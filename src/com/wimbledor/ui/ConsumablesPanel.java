package com.wimbledor.ui;

import com.wimbledor.entities.Player;
import com.wimbledor.equipment.Consumable;

import javax.swing.*;
import java.awt.*;

public class ConsumablesPanel extends JPanel {
    private final Player player;
    private final JPanel grid;
    private final JLabel goldLabel;

    public ConsumablesPanel(Player player) {
        this.player = player;
        setBorder(BorderFactory.createTitledBorder("Consumables & Gold"));
        setLayout(new BorderLayout(6,6));

        grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        goldLabel = new JLabel();

        add(grid, BorderLayout.CENTER);
        add(goldLabel, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        grid.removeAll();
        for (Consumable c : player.getConsumables()) {
            grid.add(new SlotPanel(c.getName()));
        }
        goldLabel.setText("Gold: " + player.getGold());
        revalidate();
        repaint();
    }
}
