// src/com/wimbledor/combat/CombatActions/Backstab.java
package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

public class Backstab implements ICombatAction {
    @Override
    public String getName() {
        return "Backstab";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    /** No global stat changes—everything happens in execute(). */
    @Override
    public void modifyStats(ICombatEntity actor, List<ICombatEntity> targets) {
        // no‐op
    }


    @Override
    public void execute(ICombatEntity actor, ICombatEntity target) {
        CombatUtils.AttackResult res = CombatUtils.performAttack(
                actor,
                target,
                0.5,    // halve accuracy
                true    // force crit on hit
        );
        GameContext.log(getLogMessage(
                actor, target, res.hit, res.crit, res.damage
        ));
    }
    public static void log(String msg) {
        GameContext.log(msg);
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
            return actor.getName() + " tried to fade into the shadows, but " +
                    target.getName() + " saw the " + actor.getName()+"!";
        }
        return actor.getName() + " faded from view, and then jumped at " +
                target.getName() + ", backstabbing them for " + (-amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
