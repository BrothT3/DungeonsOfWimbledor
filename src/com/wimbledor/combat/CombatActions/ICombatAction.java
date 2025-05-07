// src/com/wimbledor/combat/ICombatAction.java
package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.TurnBasedSystem.AttackResult;
import com.wimbledor.combat.enums.TargetMode;
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
     String getDescription();
    /**
     * Build a log‐friendly description of what happened.
     *
     * @param actor  who performed the action
     * @param target who it was performed on
     * @param hit    whether it actually landed
     * @param crit   whether it was a critical hit
     * @param amount how much HP was changed (negative = damage, positive = heal)
     */
    default String getLogMessage(ICombatEntity actor,
                                 ICombatEntity target,
                                 boolean hit,
                                 boolean crit,
                                 int amount) {
        // Sensible default for simple attacks:
        if (!hit) return actor.getName() + " missed " + target.getName() + "!";
        String verb = getName().toLowerCase();
        String critSuffix = crit ? " (crit!)" : "";
        if (amount < 0) {
            return actor.getName()
                    + " " + verb
                    + " " + target.getName()
                    + " for " + (-amount) + " damage"
                    + critSuffix;
        } else {
            return actor.getName()
                    + " " + verb
                    + " " + target.getName()
                    + " and healed " + amount
                    + critSuffix;
        }
    }

    default  void postExecute(ICombatEntity actor, ICombatEntity target, AttackResult result){
        /* No OP!*/
    };

    double getDurationSeconds();
}
