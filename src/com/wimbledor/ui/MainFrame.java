// src/com/wimbledor/ui/MainFrame.java
package com.wimbledor.ui;

import com.wimbledor.ui.view.NarrativePanel;
import com.wimbledor.ui.view.*;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window. Left pane swaps between CombatPanel and NarrativePanel.
 * Right pane shows persistent player info: stats, equipment, consumables, inventory, gold.
 */
public class MainFrame extends JFrame {
    private final CardLayout leftLayout;
    private final JPanel leftContainer;
    private final CombatPanel combatPanel;
    private final NarrativePanel narrativePanel;

    private final JPanel rightContainer;
    private final StatsPanel statsPanel;
    private final EquipmentPanel equipmentPanel;
    private final ConsumablesPanel consumablesPanel;
    private final InventoryButton inventoryButton;
    private final GoldPanel goldPanel;

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

        // Right: vertical stack
        rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        statsPanel = new StatsPanel();
        equipmentPanel = new EquipmentPanel();
        consumablesPanel = new ConsumablesPanel();
        inventoryButton = new InventoryButton();
        goldPanel = new GoldPanel();
        rightContainer.add(statsPanel);
        rightContainer.add(Box.createVerticalStrut(8));
        rightContainer.add(equipmentPanel);
        rightContainer.add(Box.createVerticalStrut(8));
        rightContainer.add(consumablesPanel);
        rightContainer.add(Box.createVerticalStrut(8));
        rightContainer.add(inventoryButton);
        rightContainer.add(Box.createVerticalStrut(8));
        rightContainer.add(goldPanel);
        add(new JScrollPane(rightContainer), BorderLayout.EAST);

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

    // Getters for panels so controllers can update them:
    public CombatPanel getCombatPanel() {
        return combatPanel;
    }
    public NarrativePanel getNarrativePanel() {
        return narrativePanel;
    }
    public StatsPanel getStatsPanel() {
        return statsPanel;
    }
    public EquipmentPanel getEquipmentPanel() {
        return equipmentPanel;
    }
    public ConsumablesPanel getConsumablesPanel() {
        return consumablesPanel;
    }
    public InventoryButton getInventoryButton() {
        return inventoryButton;
    }
    public GoldPanel getGoldPanel() {
        return goldPanel;
    }
}
