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
import java.util.function.BiConsumer;

public class CardController implements ActionListener {
    private final EncounterDeck deck;
    private final CardView view;
    private final MainFrame frame;
    private ICard current;
    private boolean inBattle = false;
    private TurnManager turnManager;
    public CardController(EncounterDeck deck, CardView view, MainFrame frame) {
        this.deck  = deck;
        this.view  = view;
        this.frame = frame;


    }
    private void swapCenter(JComponent comp, boolean refresh) {
        frame.setCenterComponent(comp);
        if (refresh) frame.refresh();
    }
    private void restoreNarrative() {
        swapCenter(view, true);
    }



    public void start() {
        // Register to be notified when a battle (via GameLoop) finishes:
        GameContext.setOnEncounterComplete(this::onBattleComplete);
        drawNext();
    }

    private void onBattleComplete() {
        // Battle ended: flip the flag and then advance the narrative
        inBattle = false;
        SwingUtilities.invokeLater(this::drawNext);
    }

    public void drawNext() {
        // Standard narrative draw
        if (deck.hasNext()) {
            current = deck.draw();
            view.display(current, this);
        } else {
            JOptionPane.showMessageDialog(frame, "You have cleared the dungeon!");
        }
        frame.refresh();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        ICard next = current.onOptionSelected(code);

        // 1) Entering a new battle
        if (!inBattle && next instanceof BattleCard battle) {
            inBattle = true;
            current  = battle;
            view.display(current, this);

            // launch the engine battle loop and keep its TurnManager
            this.turnManager = GameContext.startBattleWith(GameContext.getPlayer(), battle);

            // immediately swap in the player's combat‐action UI
            view.clear();
            ActionPanel ap = new ActionPanel(this.turnManager, () -> {
                GameContext.playerActionResolved();
            });
            swapCenter(ap, true);

            return;
        }

        // 2) Player clicked a combat-action button
        if (inBattle && current instanceof BattleCard) {
            // ActionPanel already executed the ICombatAction;
            // now signal the engine to advance to the next turn
            GameContext.playerActionResolved();
            return;
        }

        // 3) Narrative branches or encounter end
        if (!inBattle && next != null) {
            // still inside the same encounter → redraw it
            current = next;
            view.display(current, this);
        } else if (!inBattle) {

            // no more options → advance to the next encounter card
            drawNext();
        }

        frame.refresh();
    }
}

