package com.wimbledor.ui.view;

import com.wimbledor.entities.Player;
import com.wimbledor.equipment.EquipmentManager;

import javax.swing.*;
import java.awt.*;

/**
 * The persistent right-side panel showing player stats, equipment, consumables, and gold.
 */
public class PlayerInfoPanel extends JPanel {

    private final StatsPanel statsPanel;
    private final EquipmentPanel equipmentPanel;
    private final ConsumablesPanel consumablesPanel;
    private final GoldPanel goldPanel;
    private final InventoryButton inventoryButton;

    public PlayerInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 600));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Player Info"));

        statsPanel = new StatsPanel();
        equipmentPanel = new EquipmentPanel();
        consumablesPanel = new ConsumablesPanel();
        goldPanel = new GoldPanel();
        inventoryButton = new InventoryButton();

        add(statsPanel);
        add(Box.createVerticalStrut(5));
        add(equipmentPanel);
        add(Box.createVerticalStrut(5));
        add(consumablesPanel);
        add(Box.createVerticalStrut(5));
        add(goldPanel);
        add(Box.createVerticalStrut(10));
        add(inventoryButton);
    }

    /**
     * Updates all UI components to reflect the current player state.
     */
    public void setPlayer(Player player) {
        statsPanel.setStats(player); // Pass full player to show stats + HP
        equipmentPanel.setEquipment(EquipmentManager.getInstance());
        consumablesPanel.setConsumables(player.getTempConAct());
        goldPanel.setGold(player.getGold());
    }
}
