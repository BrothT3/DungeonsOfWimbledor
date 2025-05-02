// src/com/wimbledor/ui/ActionPanel.java
package com.wimbledor.ui;

import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionPanel extends JPanel {
    public ActionPanel(TurnManager tm, Runnable onActionComplete) {
        setLayout(new GridLayout(0,1,5,5));
        List<ICombatAction> actions = tm.getPlayerEntity()
                .getAvailableActions(tm);
        for (ICombatAction act : actions) {
            JButton btn = new JButton(act.getName());
            btn.addActionListener(e -> {
                handleActionSelection(act, tm, onActionComplete);
            });
            add(btn);
        }
    }

    private void handleActionSelection(ICombatAction act,
                                       TurnManager tm,
                                       Runnable onActionComplete) {
        List<ICombatEntity> targets;
        switch (act.getTargetMode()) {
            case SELF -> {
                targets = List.of(tm.getPlayerEntity());
                execute(act, tm, targets);
                onActionComplete.run();
                return;
            }
            case ALL_ENEMIES -> {
                targets = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                execute(act, tm, targets);
                onActionComplete.run();
                return;
            }
            case SINGLE_ENEMY -> {
                targets = tm.getEnemiesOf(tm.getPlayerEntity().getTeam());
                if (targets.size() == 1) {
                    execute(act, tm, targets);
                    onActionComplete.run();
                } else {
                    // show a simple dialog for target pick
                    ICombatEntity choice = pickSingleTarget(targets);
                    if (choice != null) {
                        execute(act, tm, List.of(choice));
                        onActionComplete.run();
                    }
                }
                return;
            }
            default -> {
                // handle other modes if any
            }
        }
    }

    private ICombatEntity pickSingleTarget(List<ICombatEntity> targets) {
        String[] names = targets.stream()
                .map(ICombatEntity::getName)
                .toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
                this, "Choose your target:", "Select Target",
                JOptionPane.PLAIN_MESSAGE, null,
                names, names[0]
        );
        if (sel == null) return null;
        return targets.stream()
                .filter(e -> e.getName().equals(sel))
                .findFirst()
                .orElse(null);
    }

    private void execute(ICombatAction act, TurnManager tm, List<ICombatEntity> targets) {
        CombatUtils.executeAction(act, tm.getPlayerEntity(), targets);
    }
}
