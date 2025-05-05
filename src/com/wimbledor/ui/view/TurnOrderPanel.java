package com.wimbledor.ui.view;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A horizontal strip showing the upcoming turn order as small icons or names.
 */
public class TurnOrderPanel extends JPanel {
    public TurnOrderPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
        setBackground(Color.BLACK);
    }

    /**
     * Rebuilds the strip to display the given actors in order.
     */
    public void setActors(List<ICombatEntity> actors) {
        removeAll();
        for (ICombatEntity e : actors) {
            // Placeholder: show the entity's name in a fixed 32×32 cell
            JLabel lbl = new JLabel(e.getName(), SwingConstants.CENTER);
            lbl.setForeground(Color.WHITE);
            lbl.setPreferredSize(new Dimension(32, 32));
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            add(lbl);
        }
        revalidate();
        repaint();
    }
}
