// src/com/wimbledor/ui/ActionPanel.java
package com.wimbledor.ui;

import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class ActionPanel extends JPanel {
    public ActionPanel() {
        // CHANGED: empty ctor; actual buttons are added in updateActions(...)
    }

    /**
     * Builds the player's action buttons *only* on their turn.
     *
     * @param tm                current TurnManager
     * @param onActionSelected  callback receives (action, targets)
     */
    public void updateActions(TurnManager tm,
                              BiConsumer<ICombatAction, List<ICombatEntity>> onActionSelected) {
        removeAll();                       // clear out old buttons
        setLayout(new BorderLayout(5,5));
        setBackground(new Color(0xFFEB3B));        // yellow
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Actions",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                getFont().deriveFont(Font.BOLD, 14f)
        ));

        // Header label
        JLabel header = new JLabel("Choose an action:", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 12f));
        add(header, BorderLayout.NORTH);

        // Container for buttons
        JPanel btnContainer = new JPanel();
        btnContainer.setBackground(new Color(0xFFEB3B));
        btnContainer.setLayout(new BoxLayout(btnContainer, BoxLayout.Y_AXIS));
        btnContainer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // CHANGED: Gather actions from tm.getPlayerEntity()
        List<ICombatAction> actions = tm.getPlayerEntity().getAvailableActions(tm);
        for (ICombatAction act : actions) {
            JButton btn = new JButton(act.getName());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 28));
            btn.setPreferredSize(new Dimension(160, 28));
            btn.addActionListener(e -> {
                // CHANGED: *Only* report the chosen action & targets,
                // UI/controller will execute it.
                List<ICombatEntity> targets;
                switch (act.getTargetMode()) {
                    case SELF -> targets = List.of(tm.getPlayerEntity());
                    case ALL_ENEMIES -> targets = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                    case SINGLE_ENEMY -> {
                        var list = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                        if (list.isEmpty()) return;
                        if (list.size() == 1) {
                            targets = list;
                        } else {
                            String[] names = list.stream()
                                    .map(ICombatEntity::getName)
                                    .toArray(String[]::new);
                            String sel = (String) JOptionPane.showInputDialog(
                                    this, "Select target:", "Target",
                                    JOptionPane.PLAIN_MESSAGE, null, names, names[0]
                            );
                            if (sel == null) return;
                            targets = List.of(
                                    list.stream()
                                            .filter(t -> t.getName().equals(sel))
                                            .findFirst()
                                            .orElse(list.get(0))
                            );
                        }
                    }
                    default -> targets = List.of();
                }
                onActionSelected.accept(act, targets);
            });
            btnContainer.add(btn);
            btnContainer.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(
                btnContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(0xFFEB3B));
        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
