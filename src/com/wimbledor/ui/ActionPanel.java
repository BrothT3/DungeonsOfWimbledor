// src/com/wimbledor/ui/ActionPanel.java
package com.wimbledor.ui;

import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Yellow‐background panel listing the player's ICombatActions.
 */
public class ActionPanel extends JPanel {

    public ActionPanel() {
        // empty ctor; actual buttons populated in updateActions(...)
    }

    /**
     * Rebuilds the action buttons for the current player turn.
     *
     * @param tm                the TurnManager driving combat
     * @param onActionComplete  called with each combat‐action's log message
     */
    public void updateActions(TurnManager tm, Consumer<String> onActionComplete) {
        removeAll();

        setLayout(new BorderLayout(5,5));
        setBackground(new Color(0xFFEB3B)); // yellow
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Actions",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                getFont().deriveFont(Font.BOLD, 14f)
        ));

        // Header
        JLabel header = new JLabel("Choose an action:", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 12f));
        add(header, BorderLayout.NORTH);

        // Button container
        JPanel btnContainer = new JPanel();
        btnContainer.setBackground(new Color(0xFFEB3B));
        btnContainer.setLayout(new BoxLayout(btnContainer, BoxLayout.Y_AXIS));
        btnContainer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        List<ICombatAction> actions = tm.getPlayerEntity().getAvailableActions(tm);
        for (ICombatAction act : actions) {
            JButton btn = new JButton(act.getName());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 28));
            btn.setPreferredSize(new Dimension(160, 28));
            btn.addActionListener(e -> {
                // determine targets
                List<ICombatEntity> targets;
                switch (act.getTargetMode()) {
                    case SELF -> {
                        targets = List.of(tm.getPlayerEntity());
                    }
                    case ALL_ENEMIES -> {
                        targets = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                    }
                    case SINGLE_ENEMY -> {
                        var list = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                        if (list.isEmpty()) {
                            // no enemies left, nothing to do
                            return;
                        }
                        if (list.size() == 1) {
                            targets = list;
                        } else {
                            ICombatEntity choice = pickSingleTarget(list);
                            if (choice == null) return;
                            targets = List.of(choice);
                        }
                    }
                    default -> { targets = List.of(); }
                }
                // execute & log
                CombatUtils.executeAction(act, GameContext.getPlayer(), targets);
                onActionComplete.accept(
                        tm.getPlayerEntity().getName()
                                + " uses " + act.getName()
                );
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

    private ICombatEntity pickSingleTarget(List<ICombatEntity> targets) {
        String[] names = targets.stream()
                .map(ICombatEntity::getName)
                .toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
                this,
                "Select target:",
                "Target",
                JOptionPane.PLAIN_MESSAGE,
                null,
                names,
                names[0]
        );
        if (sel == null) return null;
        return targets.stream()
                .filter(e -> e.getName().equals(sel))
                .findFirst()
                .orElse(null);
    }
}
