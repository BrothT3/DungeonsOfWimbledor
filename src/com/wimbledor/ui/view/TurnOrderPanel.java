package com.wimbledor.ui.view;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Displays the turn order as a horizontal strip of combat entities.
 */
public class TurnOrderPanel extends JPanel {

    public TurnOrderPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setPreferredSize(new Dimension(800, 60));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Turn Order"));
    }

    /**
     * Updates the panel with a new list of combat actors in turn order.
     */
    public void setActors(List<ICombatEntity> actors) {
        removeAll();  // Clear old labels

        for (ICombatEntity entity : actors) {
            JLabel label = new JLabel(entity.getName());  // You can customize this method
            label.setForeground(Color.YELLOW);  // Color-code if needed (e.g., team-based)
            label.setFont(new Font("Monospaced", Font.BOLD, 14));
            add(label);
        }

        revalidate();
        repaint();
    }
}
