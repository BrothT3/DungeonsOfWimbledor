package com.wimbledor.ui;

import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class EnemyPanel extends JPanel {
    private Consumer<ICombatEntity> onEnemyClicked;

    public EnemyPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.DARK_GRAY);
    }

    /** Controller calls this once, to install its callback. */
    public void setEnemyClickListener(Consumer<ICombatEntity> listener) {
        this.onEnemyClicked = listener;
    }

    /**
     * Pure‐UI rebuild of the enemy row.
     * Each EnemyCard gets a mouse‐click listener that simply
     * calls onEnemyClicked.accept(thatEnemy).
     */
    public void updateEnemies(List<ICombatEntity> enemies) {
        removeAll();
        for (ICombatEntity enemy : enemies) {
            EnemyCard card = new EnemyCard(enemy);
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onEnemyClicked != null) {
                        onEnemyClicked.accept(enemy);
                    }
                }
            });
            add(card);
        }
        revalidate();
        repaint();
    }
}