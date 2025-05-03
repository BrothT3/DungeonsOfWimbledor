// src/com/wimbledor/ui/CardController.java
package com.wimbledor.ui;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.GameContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.BiConsumer;

import static com.wimbledor.engine.GameContext.log;

public class CardController {
    private final MainFrame       frame;
    private final EncounterDeck   deck;
    private TurnManager           tm;
    private final CardView        cardView;
    private final EnemyPanel      enemyPanel;
    private final ActionPanel     actionPanel;
    private final LogPanel        logPanel;
    private ICombatAction         pendingAction;
    private ICard                 pendingAfterStage;

    public CardController(
            EncounterDeck deck,
            CardView cardView,
            EnemyPanel enemyPanel,
            ActionPanel actionPanel,
            LogPanel logPanel,
            MainFrame frame
    ) {
        this.deck              = deck;
        this.cardView          = cardView;
        this.enemyPanel        = enemyPanel;
        this.actionPanel       = actionPanel;
        this.logPanel          = logPanel;
        this.frame             = frame;
        this.pendingAfterStage = null;

        // Wire up the pure-UI panels:
        actionPanel.setActionClickListener(this::onActionClicked);
        enemyPanel .setEnemyClickListener(this::onEnemyClicked);
    }

    /** Start the first narrative card. */
    public void start() {
        drawNextCard();
    }

    /** Pull and display the next ICard from the deck. */
    private void drawNextCard() {
        ICard card = deck.draw();
        if (card == null) {
            logPanel.append("-- End of dungeon --\n");
            return;
        }
        if (card instanceof BattleCard) {
            beginBattle((BattleCard) card);
        } else {
            showNarrative(card);
        }
    }

    /** Show a narrative card and hook its buttons. */
    private void showNarrative(ICard card) {
        frame.refreshNarrativeUI();
        cardView.clear();
        cardView.display(card, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCardOptionSelected(e.getActionCommand(), card);
            }
        });
    }

    /**
     * Called when the player selects a narrative option.
     * We peek at the EncounterCard’s CardOption.next to remember
     * any “after‐battle” stage, then dispatch.
     */
    private void onCardOptionSelected(String code, ICard current) {
        // Clear any previous post-battle stage
        pendingAfterStage = null;

        if (current instanceof EncounterCard ec) {
            // Look up the CardOption matching this code
            for (CardOption opt : ec.getOptions()) {
                if (opt.getCode().equals(code)) {
                    // Capture its nextCard (may be null) for after combat
                    pendingAfterStage = opt.getNextCard();
                    break;
                }
            }
        }

        // Advance according to what onOptionSelected returns:
        ICard next = current.onOptionSelected(code);
        if (next instanceof BattleCard) {
            // Start combat (pendingAfterStage already set if there is one)
            beginBattle((BattleCard) next);
        }
        else if (next != null) {
            // Pure narrative branch – show the next EncounterCard
            showNarrative(next);
        }
        else {
            // No next stage and no battle – this encounter is done
            drawNextCard();
        }
    }

    /**
     * Kick off combat: build the TurnManager, show the enemy/action panels,
     * and pass in our BiConsumer so MainFrame can wire ActionPanel if needed.
     */
    private void beginBattle(BattleCard battleCard) {
        tm = GameContext.startBattleWith(GameContext.getPlayer(), battleCard);
        frame.refreshCombatUI(
                tm.getEnemiesOf(GameContext.getPlayer().getTeam()),
                tm,
                this::onRawActionSelected    // <— BiConsumer<ICombatAction,List<ICombatEntity>>
        );
        showPlayerActions();
    }

    /** The BiConsumer callback for raw action+targets if MainFrame ever uses it. */
    private void onRawActionSelected(
            ICombatAction action,
            List<ICombatEntity> targets
    ) {
        resolve(action, targets);
    }

    /** Populate the ActionPanel with the player’s available ICombatActions. */
    private void showPlayerActions() {
        var player = GameContext.getPlayer();
        actionPanel.updateActions(player.getAvailableActions());
    }

    /** Handle clicking any action button in ActionPanel. */
    private void onActionClicked(ICombatAction action) {
        switch (action.getTargetMode()) {
            case SELF ->
                    resolve(action, List.of(GameContext.getPlayer()));
            case ALL_ENEMIES ->
                    resolve(action,
                            tm.getEnemiesOf(GameContext.getPlayer().getTeam()));
            case SINGLE_ENEMY -> {
                List<ICombatEntity> foes =
                        tm.getEnemiesOf(GameContext.getPlayer().getTeam());
                if (foes.size() == 1) {
                    resolve(action, foes);
                } else {
                    pendingAction = action;
                    highlightEnemies(true);
                    logPanel.append("Click an enemy to target '"
                            + action.getName() + "'\n");
                }
            }
        }
    }

    /** Handle clicking an EnemyCard when awaiting a target. */
    private void onEnemyClicked(ICombatEntity enemy) {
        if (pendingAction != null) {
            resolve(pendingAction, List.of(enemy));
            pendingAction = null;
            highlightEnemies(false);
        }
    }

    /**
     * Execute the chosen action, spin AI to the next player turn (or end),
     * then refresh the UI or let onEncounterComplete fire.
     */
    private void resolve(ICombatAction action, List<ICombatEntity> targets) {
        // 1) Player action
        CombatUtils.executeAction(action, GameContext.getPlayer(), targets);
        tm.playerResolved();

        // 2) AI turns until it's back to the player or the battle ends
        ICombatEntity next;
        while ((next = tm.processNextTurn()) != null
                && next.getTeam() != Team.PLAYER) {
            // AI takes its turns
        }

        // 3) If the battle has ended, go back to narrative
        if (tm.isBattleOver()) {
            frame.refreshNarrativeUI();

            if (pendingAfterStage != null) {
                // Show the post-battle aftermath card
                showNarrative(pendingAfterStage);
                pendingAfterStage = null;
            } else {
                // No aftermath defined → advance the encounter deck
                drawNextCard();
            }
        }
        // 4) Otherwise, still in combat → refresh panels and let player act
        else {
            frame.refreshCombatUI(
                    tm.getEnemiesOf(GameContext.getPlayer().getTeam()),
                    tm,
                    this::onRawActionSelected
            );
            showPlayerActions();
        }
    }

    /** Highlight or clear an orange border around all EnemyCards. */
    private void highlightEnemies(boolean on) {
        for (Component c : frame.getEnemyPanel().getComponents()) {
            if (c instanceof JComponent jc) {
                jc.setBorder(on
                        ? BorderFactory.createLineBorder(Color.ORANGE, 3)
                        : BorderFactory.createEmptyBorder(3,3,3,3)
                );
            }
        }
    }
}
