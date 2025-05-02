// src/com/wimbledor/combat/ICombatAction.java
package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;

import java.util.List;

/**
 * Represents a single combat “move”:
 * could be a weapon swing, skill, consumable, or buff-triggered extra action.
 */
public interface ICombatAction {
    /**
     * Label shown on the button or menu.
     */
    String getName();

    /**
     * How to target: one enemy, all enemies, self, etc.
     */
    TargetMode getTargetMode();

    /**
     * Apply the action’s effect to a single target.
     */
    void execute(ICombatEntity actor, ICombatEntity target);

    /**
     * Hook to let this action tweak stats before it runs
     * (e.g. temporary +10% accuracy, +5 penetration, etc.).
     * Default is no-op; override in actions that buff/debuff stats.
     */
    default void modifyStats(ICombatEntity actor, List<ICombatEntity> targets) {
        // no-op
    }
}
