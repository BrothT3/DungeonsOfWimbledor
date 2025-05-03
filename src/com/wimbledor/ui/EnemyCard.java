package com.wimbledor.ui;

import com.wimbledor.entities.ICombatEntity;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EnemyCard extends JPanel {
    private final ICombatEntity enemy;
    private final JLabel nameLabel;
    private final JProgressBar hpBar;

    public EnemyCard(ICombatEntity enemy) {
        this.enemy = enemy;
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(100, 80));
        setBackground(new Color(0x2E2E2E));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                enemy.getName(),
                TitledBorder.CENTER,
                TitledBorder.TOP,
                getFont().deriveFont(Font.BOLD, 12f),
                Color.WHITE
        ));

        // Name label (redundant since border shows name, but kept for consistency)
        nameLabel = new JLabel(enemy.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(getFont().deriveFont(Font.PLAIN, 10f));
        add(nameLabel, BorderLayout.NORTH);

        // HP bar
        hpBar = new JProgressBar(0, enemy.getMaxHp());
        hpBar.setValue(enemy.getCurrentHp());
        hpBar.setStringPainted(true);
        hpBar.setForeground(Color.RED);
        hpBar.setBackground(Color.DARK_GRAY);
        add(hpBar, BorderLayout.CENTER);

        // Optionally, additional stats could go here
    }

    /**
     * Refresh the card to reflect updated HP or other stats.
     */
    public void refresh() {
        hpBar.setMaximum(enemy.getMaxHp());
        hpBar.setValue(enemy.getCurrentHp());
        hpBar.setString(enemy.getCurrentHp() + " / " + enemy.getMaxHp());
        repaint();
    }

    public ICombatEntity getEnemy() {
        return enemy;
    }
}