package com.wimbledor.ui;

import com.wimbledor.entities.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {
    private final StatsPanel statsPanel;
    private final EquipmentPanel equipmentPanel;
    private final ConsumablesPanel consumablesPanel;

    public PlayerInfoPanel(Player player) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        statsPanel = new StatsPanel(player);
        equipmentPanel = new EquipmentPanel(player);
        consumablesPanel = new ConsumablesPanel(player);

        add(statsPanel);
        add(Box.createVerticalStrut(15));
        add(equipmentPanel);
        add(Box.createVerticalStrut(15));
        add(consumablesPanel);
    }

    public void refresh() {
        statsPanel.refresh();
        equipmentPanel.refresh();
        consumablesPanel.refresh();
    }
}
