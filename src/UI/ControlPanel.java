// File: UI/ControlPanel.java
package UI;

import Cards.BaseCard;
import Cards.EncounterCard;
import Cards.EncounterStage;
import Cards.StageOption;
import GameWorld.*;
import GameWorld.Player;
import GameWorld.TurnManager;
import GameWorld.Interfaces.ICombatAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ControlPanel extends JPanel {
    private final JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

    // injected handlers:
    private Consumer<String>        onEventChoice;
    private Consumer<ICombatAction> onCombatAction;

    public ControlPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(actions, BorderLayout.CENTER);
    }

    /** Call this once at startup to hook your UI into game logic. */
    public void setEncounterHandler(Consumer<String> evHandler) {
        this.onEventChoice = evHandler;
    }
    public void setCombatHandler(Consumer<ICombatAction> cbHandler) {
        this.onCombatAction = cbHandler;
    }

    /**
     * Rebuilds the buttons in the panel for either:
     *  • an EncounterCard (branching choices)
     *  • or, if in a battle and it's the player's turn, that player's combat actions.
     */
    public void updateControls(BaseCard card,
                               Player player,
                               TurnManager tm) {
        actions.removeAll();

        // 1) Encounter flow: show each StageOption
        if (card instanceof EncounterCard enc && onEventChoice != null) {
            EncounterStage stage = enc.getCurrentStage();
            for (StageOption opt : stage.getOptions()) {
                JButton btn = new JButton(opt.getLabel());
                btn.setActionCommand(opt.getCode());
                btn.addActionListener(e -> onEventChoice.accept(opt.getCode()));
                actions.add(btn);
            }

            // 2) Combat flow: mid‐combat and it's _your_ turn?
        } else if (tm != null && tm.getCurrentEntity() instanceof Player) {
            List<ICombatAction> acts = player.getActions();
            for (ICombatAction act : acts) {
                JButton btn = new JButton(act.getLabel());
                btn.addActionListener(e -> onCombatAction.accept(act));
                actions.add(btn);
            }
        }

        actions.revalidate();
        actions.repaint();
    }
}
