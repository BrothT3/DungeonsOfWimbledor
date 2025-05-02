// src/com/wimbledor/ui/CardController.java
package com.wimbledor.ui;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Glue between narrative (CardView) and combat panels.
 */
public class CardController implements ActionListener {
    private final EncounterDeck deck;
    private final CardView      cardView;
    private final EnemyPanel    enemyPanel;
    private final ActionPanel   actionPanel;
    private final LogPanel      logPanel;
    private final MainFrame     frame;

    private ICard       current;
    private boolean     inBattle    = false;
    private TurnManager turnManager;

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

        GameContext.setOnEncounterComplete(this::onBattleComplete);
    }

    public void start() {
        drawNext();
    }

    private void onBattleComplete() {
        inBattle = false;
        SwingUtilities.invokeLater(this::drawNext);
    }

    private void drawNext() {
        enemyPanel .setVisible(false);
        actionPanel.setVisible(false);
        logPanel   .setVisible(false);

        cardView.setVisible(true);

        if (deck.hasNext()) {
            current = deck.draw();
            cardView.display(current, this);
        } else {
            JOptionPane.showMessageDialog(frame, "You cleared the dungeon!");
        }
        frame.refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        ICard next  = current.onOptionSelected(code);

        // 1) start battle
        if (!inBattle && next instanceof BattleCard battle) {
            inBattle = true;
            current  = battle;
            cardView.setVisible(false);

            enemyPanel.updateEnemies(battle.getMonsters());
            enemyPanel.setVisible(true);

            logPanel.clear();
            logPanel.setVisible(true);

            turnManager = GameContext.startBattleWith(
                    GameContext.getPlayer(),
                    battle
            );

            actionPanel.updateActions(
                    turnManager,
                    logLine -> {
                        logPanel.append(logLine);
                        GameContext.playerActionResolved();
                    }
            );
            actionPanel.setVisible(true);

            frame.refresh();
            return;
        }

        // 2) in‐battle button clicks are handled inside ActionPanel
        if (inBattle && next instanceof BattleCard) {
            return;
        }

        // 3) narrative
        if (!inBattle && next != null) {
            current = next;
            cardView.display(current, this);
        } else if (!inBattle) {
            drawNext();
        }

        frame.refresh();
    }
}
