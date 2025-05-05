package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the player's gold amount.
 */
public class GoldPanel extends JPanel {
    private final JLabel goldLabel;

    public GoldPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Gold"));

        goldLabel = new JLabel("Gold: 0");
        goldLabel.setForeground(Color.YELLOW);
        goldLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        add(goldLabel);
    }

    public void setGold(int amount) {
        goldLabel.setText("Gold: " + amount);
    }
}
