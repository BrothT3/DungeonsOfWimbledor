package com.wimbledor.ui;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
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
    private final MainFrame frame;
    private final EncounterDeck deck;
    private TurnManager tm;
    private final CardView cardView;
    private final EnemyPanel enemyPanel;
    private final ActionPanel actionPanel;
    private final LogPanel logPanel;
    private ICombatAction pendingAction;

    public CardController(
            EncounterDeck deck,
            CardView cardView,
            EnemyPanel enemyPanel,
            ActionPanel actionPanel,
            LogPanel logPanel,
            MainFrame frame
    ) {
        this.deck = deck;
        this.cardView = cardView;
        this.enemyPanel = enemyPanel;
        this.actionPanel = actionPanel;
        this.logPanel = logPanel;
        this.frame = frame;

        // UI event wiring
        GameContext.setOnEncounterComplete(() -> SwingUtilities.invokeLater(() -> {
            frame.refreshNarrativeUI();
            drawNextCard();
        }));

        // UI event wiring
        actionPanel.setActionClickListener(this::onActionClicked);
        enemyPanel.setEnemyClickListener(this::onEnemyClicked);
    }

    /** Start the dungeon crawl. */
    public void start() {
        drawNextCard();
    }

    /** Pull & display next card from deck. */
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

    /** Display narrative card. */
    private void showNarrative(ICard card) {
        frame.refreshNarrativeUI();
        cardView.clear();
        // display card and listen for button clicks
        cardView.display(card, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCardOptionSelected(e.getActionCommand(), card);
            }
        });
    }

    /** Handle narrative option click. */
    private void onCardOptionSelected(String code, ICard current) {
        ICard next = current.onOptionSelected(code);
        if (next instanceof BattleCard) {
            beginBattle((BattleCard) next);
        } else if (next != null) {
            showNarrative(next);
        } else {
            drawNextCard();
        }
    }

    /** Initialize and display combat UI. */
    private void beginBattle(BattleCard battleCard) {
        this.tm = GameContext.startBattleWith(GameContext.getPlayer(), battleCard);
        tm = GameContext.startBattleWith(GameContext.getPlayer(), battleCard);

        frame.refreshCombatUI(
                tm.getEnemiesOf(GameContext.getPlayer().getTeam()),
                tm,
                this::onRawActionSelected
        );
        showPlayerActions();
    }

    /** Callback for controller-driven action & target. */
    private void onRawActionSelected(ICombatAction action, List<ICombatEntity> targets) {
        resolve(action, targets);
    }

    /** Handle action button clicks. */
    private void onActionClicked(ICombatAction action) {
        switch (action.getTargetMode()) {
            case SELF -> resolve(action, List.of(GameContext.getPlayer()));
            case ALL_ENEMIES -> resolve(action, tm.getEnemiesOf(GameContext.getPlayer().getTeam()));
            case SINGLE_ENEMY -> {
                List<ICombatEntity> foes = tm.getEnemiesOf(GameContext.getPlayer().getTeam());
                if (foes.size() == 1) {
                    resolve(action, foes);
                } else {
                    pendingAction = action;
                    highlightEnemies(true);
                    logPanel.append("Click an enemy to target ‘" + action.getName() + "’\n");
                }
            }
        }
    }

    /** Handle enemy card clicks when awaiting a target. */
    private void onEnemyClicked(ICombatEntity enemy) {
        if (pendingAction != null) {
            resolve(pendingAction, List.of(enemy));
            pendingAction = null;
            highlightEnemies(false);
        }
    }

    /** Execute player action, spin AI turns, then refresh or end battle. */
    private void resolve(ICombatAction action, List<ICombatEntity> targets) {
        CombatUtils.executeAction(action, GameContext.getPlayer(), targets);
        tm.playerResolved();

        ICombatEntity next;
        while ((next = tm.processNextTurn()) != null && next.getTeam() != Team.PLAYER) {
            // AI turn
        }

        if (tm.isBattleOver()) {
            // turnManager's onBattleOver will invoke endBattle()
        } else {
            showPlayerActions();
            frame.refreshCombatUI(
                    tm.getEnemiesOf(GameContext.getPlayer().getTeam()),
                    tm,
                    this::onRawActionSelected
            );
        }
    }

    /** Highlight or un-highlight all enemy cards. */
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
    private void showPlayerActions() {
        var player = GameContext.getPlayer();
        actionPanel.updateActions(
                player.getAvailableActions()
        );
    }
    /** Called when battle ends. */
    private void endBattle() {
        SwingUtilities.invokeLater(() -> {
            log("-- Battle Over --\n");
            frame.refreshNarrativeUI();
            drawNextCard();
        });
    }
}
