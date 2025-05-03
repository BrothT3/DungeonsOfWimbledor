// src/com/wimbledor/ui/CardController.java
package com.wimbledor.ui;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController implements ActionListener {
    private final EncounterDeck deck;
    private final CardView      cardView;
    private final EnemyPanel    enemyPanel;
    private ActionPanel         actionPanel;   // MODIFIED: now rebuilt each turn
    private final LogPanel      logPanel;
    private final MainFrame     frame;

    private ICard        current;
    private boolean      inBattle     = false;
    private TurnManager  turnManager;

    public CardController(EncounterDeck deck,
                          CardView cardView,
                          EnemyPanel enemyPanel,
                          ActionPanel actionPanel,
                          LogPanel logPanel,
                          MainFrame frame) {
        this.deck        = deck;
        this.cardView    = cardView;
        this.enemyPanel  = enemyPanel;
        this.actionPanel = actionPanel;
        this.logPanel    = logPanel;
        this.frame       = frame;

        GameContext.setOnEncounterComplete(this::drawNext);
    }

    public void start() {
        drawNext();
    }

    private void drawNext() {
        // hide combat panels
        enemyPanel .setVisible(false);
        actionPanel.setVisible(false);
        logPanel   .setVisible(false);

        // show narrative
        cardView   .setVisible(true);

        if (deck.hasNext()) {
            current = deck.draw();
            cardView.display(current, this);
        } else {
            JOptionPane.showMessageDialog(frame, "You've cleared the dungeon!");
        }
        frame.refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        ICard next  = current.onOptionSelected(code);

        // 1) Start battle
        if (!inBattle && next instanceof BattleCard battle) {
            inBattle = true;
            current  = battle;
            cardView.setVisible(false);
            logPanel.clear();

            enemyPanel.updateEnemies(battle.getMonsters());
            enemyPanel.setVisible(true);

            // build a fresh TurnManager
            turnManager = GameContext.startBattleWith(
                    GameContext.getPlayer(), battle
            );

            // CHANGED: spin AI until it's *actually* the player's turn
            ICombatEntity who;
            while ((who = turnManager.processNextTurn()) != null
                    && who.getTeam() != Team.PLAYER) {
                // AI turn happens inside takeTurn() + logs itself
            }

            // now show buttons *only* on player's turn
            showPlayerActions();
            return;
        }

        // 2) Narrative branching (no-op here; handled by CardView)
        if (!inBattle) {
            if (next != null) {
                current = next;
                cardView.display(current, this);
            } else {
                drawNext();
            }
            frame.refresh();
        }
    }

    /**
     * Rebuilds and shows the action buttons for the player’s turn.
     */
    private void showPlayerActions() {
        enemyPanel.setVisible(true);
        logPanel  .setVisible(true);

        // CHANGED: recreate ActionPanel each turn
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        ActionPanel fresh = new ActionPanel();
        fresh.updateActions(
                turnManager,
                (act, targets) -> {
                    // 1) execute & log
                    CombatUtils.executeAction(
                            act,
                            turnManager.getPlayerEntity(),
                            targets
                    );
                    // 2) tell the TM we're done
                    turnManager.playerResolved();
                    // 3) spin AI again
                    ICombatEntity who;
                    while ((who = turnManager.processNextTurn()) != null
                            && who.getTeam() != Team.PLAYER) { }
                    // 4) if battle still on, rebuild buttons
                    if (!turnManager.isBattleOver()) {
                        showPlayerActions();
                    }
                }
        );

        center.add(fresh);
        frame.refreshCombatUI(
                turnManager.getEnemiesOf(turnManager.getPlayerEntity().getTeam()),
                turnManager,
                (act, targets) -> {
                    // execute & log
                    CombatUtils.executeAction(act,
                            turnManager.getPlayerEntity(),
                            targets);
                    // advance past player
                    turnManager.playerResolved();
                    // spin AI
                    ICombatEntity who;
                    while ((who = turnManager.processNextTurn()) != null
                            && who.getTeam() != Team.PLAYER) { }
                    // if battle still on, rebuild
                    if (!turnManager.isBattleOver()) {
                        showPlayerActions();
                    }
                }
        );
        frame.refresh();
    }
}
