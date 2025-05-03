package com.wimbledor.combat.CombatActions;



import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

public class MagicMissile implements ICombatAction {
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
                1,
                true    // force crit on hit
        );
        if (res.hit){
            int dmg = (int)(actor.getKnowledge() * 1.5 + actor.getWillpower() * 0.5);
            CombatUtils.applyDamage(actor, target, dmg);
        }

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
            return actor.getName() + " attempted to cast something " +
                    target.getName() + " but the spell fizzled " + actor.getName()+"!";
        }
        return actor.getName() + " condenses a small amount of mana into a maggic missile and shoots it at " +
                target.getName() + ", dealing" + (-amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
