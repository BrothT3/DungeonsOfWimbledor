package com.wimbledor.ui.view;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays enemy combatants in battle, and allows them to be selected.
 */
public class EnemyViewPanel extends JPanel {

    private final List<ICombatEntity> currentEnemies = new ArrayList<>();
    private final List<ICombatEntity> selectedEnemies = new ArrayList<>();

    public EnemyViewPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(800, 140));
        setBorder(BorderFactory.createTitledBorder("Enemies"));
    }

    /**
     * Render the given list of enemies in the panel.
     */
    public void setEnemies(List<ICombatEntity> enemies) {
        currentEnemies.clear();
        currentEnemies.addAll(enemies);
        selectedEnemies.clear();

        removeAll();

        for (ICombatEntity enemy : enemies) {
            JPanel card = createEnemyCard(enemy);
            add(card);
        }

        revalidate();
        repaint();
    }

    /**
     * Return the selected enemies for targeting.
     */
    public List<ICombatEntity> getSelectedEntities() {
        return new ArrayList<>(selectedEnemies);
    }

    /**
     * Clears any current selection (called after action confirmation).
     */
    public void clearSelection() {
        selectedEnemies.clear();
        setEnemies(currentEnemies);  // Re-render to reset visuals
    }

    /**
     * Creates a visual card component for an enemy.
     */
    private JPanel createEnemyCard(ICombatEntity enemy) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(120, 100));
        card.setBackground(Color.GRAY);
        card.setBorder(new LineBorder(Color.WHITE, 2));
        card.setLayout(new BorderLayout());

        JLabel name = new JLabel(enemy.getName(), SwingConstants.CENTER);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(name, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedEnemies.contains(enemy)) {
                    selectedEnemies.remove(enemy);
                    card.setBorder(new LineBorder(Color.WHITE, 2));
                } else {
                    selectedEnemies.add(enemy);
                    card.setBorder(new LineBorder(Color.YELLOW, 3));
                }
            }
        });

        return card;
    }
}
