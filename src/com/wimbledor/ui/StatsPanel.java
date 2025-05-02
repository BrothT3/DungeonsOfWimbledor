package com.wimbledor.ui;

import com.wimbledor.entities.Player;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private final Player player;
    private final JLabel[] valueLabels;
    private static final String[] NAMES = {
            "HP", "Attack", "Defense", "Penetration",
            "Accuracy", "Evasion", "Speed", "Crit %", "Crit ×"
    };

    public StatsPanel(Player player) {
        this.player = player;
        setBorder(BorderFactory.createTitledBorder("Stats"));
        setLayout(new GridLayout(NAMES.length, 2, 4, 4));

        valueLabels = new JLabel[NAMES.length];
        for (int i = 0; i < NAMES.length; i++) {
            add(new JLabel(NAMES[i] + ":"));
            valueLabels[i] = new JLabel();
            add(valueLabels[i]);
        }
        refresh();
    }

    public void refresh() {
        int idx = 0;
        valueLabels[idx++].setText(player.getCurrentHp() + "/" + player.getMaxHp());
        valueLabels[idx++].setText(String.valueOf(player.getAttack()));
        valueLabels[idx++].setText(String.valueOf(player.getDefense()));
        valueLabels[idx++].setText(String.valueOf(player.getDefensePenetration()));
        valueLabels[idx++].setText(String.valueOf(player.getAccuracy()));
        valueLabels[idx++].setText(String.valueOf(player.getEvasion()));
        valueLabels[idx++].setText(String.valueOf(player.getSpeed()));
        valueLabels[idx++].setText(player.getCritChance() + "%");
        valueLabels[idx++].setText("×" + player.getCritMultiplier());
    }
}
