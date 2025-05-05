package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Displays player HP, numeric/horizontal bar, six base stats, and status icons.
 */
public class StatsPanel extends JPanel {
    private final JProgressBar hpBar;
    private final JLabel       hpLabel;
    private final JPanel       statsGrid;

    public StatsPanel() {
        setLayout(new BorderLayout(2,2));
        hpBar = new JProgressBar(0,100);
        hpLabel = new JLabel("HP: 0/0");
        statsGrid = new JPanel(new GridLayout(3,2));
        // add stat labels: STR, DEX, END, WIL, INT, CUN
        for (String stat : new String[]{"STR","DEX","END","WIL","INT","CUN"}) {
            statsGrid.add(new JLabel(stat + ": 0"));
        }
        add(hpLabel, BorderLayout.NORTH);
        add(hpBar, BorderLayout.CENTER);
        add(statsGrid, BorderLayout.SOUTH);
    }

    public void updateHp(int current, int max) {
        hpBar.setMaximum(max);
        hpBar.setValue(current);
        hpLabel.setText("HP: " + current + "/" + max);
    }

    public void updateStats(int str,int dex,int end,int wil,int intel,int cun) {
        Component[] comps = statsGrid.getComponents();
        ((JLabel)comps[0]).setText("STR: " + str);
        ((JLabel)comps[1]).setText("DEX: " + dex);
        ((JLabel)comps[2]).setText("END: " + end);
        ((JLabel)comps[3]).setText("WIL: " + wil);
        ((JLabel)comps[4]).setText("INT: " + intel);
        ((JLabel)comps[5]).setText("CUN: " + cun);
    }
}
