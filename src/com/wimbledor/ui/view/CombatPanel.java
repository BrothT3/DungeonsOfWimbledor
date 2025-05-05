// src/com/wimbledor/ui/view/CombatPanel.java
package com.wimbledor.ui.view;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.ui.view.EnemyViewPanel;
import com.wimbledor.ui.view.ActionListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main combat view: shows turn order, enemies, actions, and combat log.
 */
public class CombatPanel extends JPanel {
    private final TurnOrderPanel turnOrder;
    private final EnemyViewPanel enemies;
    private final ActionListPanel actions;
    private final LogPanel combatLog;

    public CombatPanel() {
        setLayout(new BorderLayout(4, 4));

        // Turn order strip at the top
        turnOrder = new TurnOrderPanel();
        add(turnOrder, BorderLayout.NORTH);

        // Split center into enemies (top) and actions+log (bottom)
        enemies = new EnemyViewPanel();
        actions = new ActionListPanel();
        combatLog = new LogPanel();

        JSplitPane mid = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mid.setTopComponent(enemies);

        JPanel bottom = new JPanel(new BorderLayout(2, 2));
        bottom.add(actions, BorderLayout.NORTH);
        bottom.add(new JScrollPane(combatLog), BorderLayout.CENTER);
        mid.setBottomComponent(bottom);
        mid.setResizeWeight(0.6);

        add(mid, BorderLayout.CENTER);
    }

    /** Update the turn order display with the current actors. */
    public void updateTurnOrder(List<ICombatEntity> actors) {
        turnOrder.setActors(actors);
    }

    /** Update the enemy panel with all living foes. */
    public void updateEnemies(List<ICombatEntity> foes) {
        enemies.setEnemies(foes);
    }

    /** Update the list of available actions for the player. */
    public void updateActions(List<ICombatAction> actionsList) {
        actions.setActions(actionsList);
    }

    /** Append a line to the combat log. */
    public void appendLog(String line) {
        combatLog.append(line);
    }

    /** Clear all existing log lines. */
    public void clearLog() {
        combatLog.clear();
    }

    /**
     * Register a callback for when the player clicks an action button.
     * The listener receives the chosen action; the panel handles target selection.
     */
    public void setActionClickListener(ActionListPanel.ActionClickListener listener) {
        actions.setActionClickListener(listener);
    }

    /**
     * Returns the currently selected enemies for a single-target action.
     */
    public List<ICombatEntity> getSelectedEnemies() {
        return enemies.getSelectedEntities();
    }
    public void clearSelection() {
        // delegate to the EnemyViewPanel
        enemies.clearSelection();
    }
}
