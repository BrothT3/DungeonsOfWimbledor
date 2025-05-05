package com.wimbledor.ui;

import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.NarrativePanel;
import com.wimbledor.ui.view.PlayerInfoPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window. Left pane swaps between CombatPanel and NarrativePanel.
 * Right pane shows persistent player info via PlayerInfoPanel.
 */
public class MainFrame extends JFrame {
    private final CardLayout leftLayout;
    private final JPanel leftContainer;
    private final CombatPanel combatPanel;
    private final NarrativePanel narrativePanel;

    private final PlayerInfoPanel playerInfoPanel;

    public MainFrame() {
        super("Dungeons of WimbleDor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Left: CardLayout for Combat vs Narrative
        leftLayout = new CardLayout();
        leftContainer = new JPanel(leftLayout);
        combatPanel = new CombatPanel();
        narrativePanel = new NarrativePanel();
        leftContainer.add(combatPanel, "COMBAT");
        leftContainer.add(narrativePanel, "NARRATIVE");
        add(leftContainer, BorderLayout.CENTER);

        // Right: PlayerInfoPanel encapsulates all persistent player UI
        playerInfoPanel = new PlayerInfoPanel();
        add(new JScrollPane(playerInfoPanel), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Show the combat UI on the left. */
    public void showCombat() {
        leftLayout.show(leftContainer, "COMBAT");
    }

    /** Show the narrative UI on the left. */
    public void showNarrative() {
        leftLayout.show(leftContainer, "NARRATIVE");
    }

    // Getters for main sub-panels
    public CombatPanel getCombatPanel() {
        return combatPanel;
    }

    public NarrativePanel getNarrativePanel() {
        return narrativePanel;
    }

    public PlayerInfoPanel getPlayerInfoPanel() {
        return playerInfoPanel;
    }
}
