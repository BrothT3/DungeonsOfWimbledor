package com.wimbledor.ui.view;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shows each enemy as a clickable card with name and HP.
 */
public class EnemyViewPanel extends JPanel {
    private ICombatEntity selected;
    private final List<ICombatEntity> enemies = new ArrayList<>();

    public EnemyViewPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.DARK_GRAY);
    }

    /**
     * Populate the panel with the given living enemies.
     */
    public void setEnemies(List<ICombatEntity> foes) {
        enemies.clear();
        enemies.addAll(foes);
        selected = null;
        removeAll();
        for (ICombatEntity e : foes) {
            JPanel card = createEnemyCard(e);
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    selected = e;
                    // highlight border
                    card.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
                }
            });
            add(card);
        }
        revalidate();
        repaint();
    }
    /**
     * Returns the single selected enemy, or empty if none.
     */
    public List<ICombatEntity> getSelectedEntities() {
        return selected == null ? Collections.emptyList() : List.of(selected);
    }

    private JPanel createEnemyCard(ICombatEntity enemy) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(80, 80));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setBackground(Color.BLACK);

        JLabel name = new JLabel(enemy.getName(), SwingConstants.CENTER);
        name.setForeground(Color.WHITE);
        JLabel hp   = new JLabel(
                enemy.getCurrentHp() + "/" + enemy.getMaxHp(),
                SwingConstants.CENTER
        );
        hp.setForeground(Color.RED);

        card.add(name, BorderLayout.NORTH);
        card.add(hp, BorderLayout.SOUTH);
        return card;
    }
    public void clearSelection() {
        selected = null;
        // also clear any border highlights:
        for (Component c : getComponents()) {
            if (c instanceof JComponent jc) {
                jc.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        }
    }
}