// src/com/wimbledor/combat/CombatActions/Slash.java
package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

/**
 * A basic melee attack.
 * Uses full accuracy, no forced crit.
 */
public class Slash implements ICombatAction {
    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public void modifyStats(ICombatEntity actor, java.util.List<ICombatEntity> targets) {
        // no temporary stat tweaks
    }

    @Override
    public void execute(ICombatEntity actor, ICombatEntity target) {
        // performAttack(auto-logs hit/crit/dmg consistently)
        CombatUtils.AttackResult result =
                CombatUtils.performAttack(actor, target,
                        /* accuracyMultiplier */ 1.0,
                        /*Dmg Multiplier*/ 1,
                        /* forceCrit */ false

                );

        // log via GameContext.log (called inside performAttack? or here)
        GameContext.log(
                getLogMessage(
                        actor,
                        target,
                        result.hit,
                        result.crit,
                        result.damage
                )
        );
    }

    @Override
    public String getLogMessage(
            ICombatEntity actor,
            ICombatEntity target,
            boolean hit,
            boolean crit,
            int amount
    ) {
        if (!hit) {
            return actor.getName() + " tried to slash at " +target.getName() +", but missed!";
        }
        return actor.getName() + " swings his sword and slashes " +
                target.getName() + " for " + (-amount) + " damage" +
                (crit ? " (Critical!)" : "");
    }
}
