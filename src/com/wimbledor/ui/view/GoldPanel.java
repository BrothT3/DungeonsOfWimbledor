package com.wimbledor.ui.view;

import javax.swing.*;

/**
 * Displays the player's gold amount.
 */
public class GoldPanel extends JPanel {
    private final JLabel goldLabel;
    public GoldPanel() {
        goldLabel = new JLabel("Gold: 0");
        add(goldLabel);
    }
    public void updateGold(int amount) {
        goldLabel.setText("Gold: " + amount);
    }
}