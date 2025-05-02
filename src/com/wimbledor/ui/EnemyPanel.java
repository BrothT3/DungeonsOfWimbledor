// src/com/wimbledor/ui/EnemyPanel.java
package com.wimbledor.ui;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Displays a horizontal row of enemy “cards” in purple.
 */
public class EnemyPanel extends JPanel {
    public EnemyPanel(List<ICombatEntity> enemies) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(128, 0, 128)); // dark purple
        updateEnemies(enemies);
    }

    /** Rebuilds the panel to show the given enemies. */
    public void updateEnemies(List<ICombatEntity> enemies) {
        removeAll();
        for (ICombatEntity e : enemies) {
            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(100, 100));
            card.setBackground(new Color(160, 32, 240)); // lighter purple
            card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

            JLabel name = new JLabel(e.getName(), SwingConstants.CENTER);
            name.setForeground(Color.WHITE);
            name.setFont(name.getFont().deriveFont(Font.BOLD, 12f));
            card.add(name);

            add(card);
        }
        revalidate();
        repaint();
    }
}
