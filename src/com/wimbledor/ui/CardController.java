package com.wimbledor.ui;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.GameContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController implements ActionListener {
    private final EncounterDeck deck;
    private final CardView view;
    private final MainFrame frame;
    private ICard current;
    private boolean inBattle = false;

    public CardController(EncounterDeck deck, CardView view, MainFrame frame) {
        this.deck  = deck;
        this.view  = view;
        this.frame = frame;
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

        if (!inBattle && next instanceof BattleCard battle) {
            System.out.println("Entering battle with " + battle);
            // 1) Enter battle state
            inBattle = true;
            current  = battle;
            view.display(current, this);

            // 2) Kick off the GameLoop / TurnManager
            GameContext.startBattleWith(GameContext.getPlayer(), battle);

            // Don't call playerResolved here—battle launch isn't a player turn.
        }
        else if (!inBattle && next != null) {
            System.out.println("Narrative branch to " + next);
            // Narrative branch/stage change
            current = next;
            view.display(current, this);
        }
        else if (!inBattle) {
            System.out.println("Encounter ended, drawing next");
            // Encounter over: move to next
            drawNext();
        }
        else {
            System.out.println("In battle, player action resolved");
            // inBattle == true: this must be a player combat action
            // Execute it via the view/current card logic
            // (your BattleCard.getOptions() effects should have already run)

            // Tell the engine "the player has finished their turn"
            GameContext.playerActionResolved();
        }

        // Finally, refresh to show updated HP/buffs/etc.
        frame.refresh();
    }
}
