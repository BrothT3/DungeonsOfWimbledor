package com.wimbledor.combat;


import com.wimbledor.effects.Buff;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Central combat resolution:
 * - Honors buff hooks
 * - Honors any defense penetration
 * - Calls action.modifyStats(...) before execution
 */
public class CombatUtils {
    private static final Random RNG = new Random();

    /**
     * Resolves a “basic attack” from attacker → defender,
     * including buff hooks and penetration.
     */
    public static void resolveAttack(ICombatEntity attacker, ICombatEntity defender) {
        // Pre‐attack / pre‐defend hooks
        for (Buff b : attacker.getBuffs()) {
            b.onPreAttack(attacker, defender);
        }
        for (Buff b : defender.getBuffs()) {
            b.onPreDefend(defender, attacker);
        }

        // Gather stats
        int atk = attacker.getAttack();
        int def = defender.getDefense();

        // Penetration
        int pen = attacker.getDefensePenetration();
        def = Math.max(0, def - pen);

        // Hit roll
        boolean hit = Math.random() * 100 < (attacker.getAccuracy() - defender.getEvasion());
        boolean crit = false;
        int damage = 0;
        if (hit) {
            // Crit check
            crit = Math.random() * 100 < attacker.getCritChance();
            // Damage calculation (attack minus defense, times crit multiplier)
            damage = (int) (Math.max(0, atk - def) * (crit ? attacker.getCritMultiplier() : 1));
            defender.applyDamage(damage);
        }

        // Post‐attack / post‐defend hooks
        for (Buff b : attacker.getBuffs()) {
            b.onPostAttack(attacker, defender, hit, crit, damage);
        }
        for (Buff b : defender.getBuffs()) {
            b.onPostDefend(defender, attacker, hit, crit, damage);
        }
    }


    /**
     * Wraps any ICombatAction:
     * 1) Buff hooks (onActionStart)
     * 2) action.modifyStats(...) for temp stat shifts
     * 3) action.execute(...) actual effect
     * 4) Buff hooks (onActionEnd)
     */
   /* public static void executeAction(ICombatAction action,
                                     ICombatEntity actor,
                                     List<ICombatEntity> targets) {
        // Pre-action buff hooks
        for (Buff b : actor.getBuffs()) b.onActionStart(actor, action);
        for (ICombatEntity tgt : targets)
            for (Buff b : tgt.getBuffs()) b.onActionStart(tgt, action);

        // Let the action tweak stats (e.g. +20% damage, +5 pen, etc.)
        action.modifyStats(actor, targets);

        // Actually apply it
        for (ICombatEntity tgt : targets) {
            action.execute(actor, tgt);
        }

        // Post-action hooks
        for (Buff b : actor.getBuffs()) b.onActionEnd(actor, action);
        for (ICombatEntity tgt : targets)
            for (Buff b : tgt.getBuffs()) b.onActionEnd(tgt, action);
    }*/
    /**
     * Executes an ICombatAction and logs each target result.
     *
     * @param action    the combat action to run
     * @param actor     the entity performing the action
     * @param targets   the list of targets
     */
    public static void executeAction(ICombatAction action,
                                     ICombatEntity actor,
                                     List<ICombatEntity> targets) {
        // 1) Buff hooks (start)
        for (Buff b : actor.getBuffs())        b.onActionStart(actor, action);
        for (ICombatEntity tgt : targets)
            for (Buff b : tgt.getBuffs())      b.onActionStart(tgt, action);

        // 2) Stat tweaks
        action.modifyStats(actor, targets);

        // 3) For each target, run and log
        for (ICombatEntity tgt : targets) {
            int beforeHp = tgt.getCurrentHp();

            // Actual effect (this might call resolveAttack internally)
            action.execute(actor, tgt);

            int afterHp  = tgt.getCurrentHp();
            int delta    = afterHp - beforeHp;    // negative = damage, positive = heal

            // If your basic‐attack actions use CombatUtils.resolveAttack,
            // you can extend resolveAttack to return a (hit,crit,damage) struct.
            // Here we’ll detect “hit” simply as delta != 0.
            boolean hit  = delta != 0;
            boolean crit = false; // you can wire this up if resolveAttack reports it

            // Let the action build its own log message:
            String msg = action.getLogMessage(actor, tgt, hit, crit, delta);
            GameContext.log(msg);
        }

        // 4) Buff hooks (end)
        for (Buff b : actor.getBuffs())        b.onActionEnd(actor, action);
        for (ICombatEntity tgt : targets)
            for (Buff b : tgt.getBuffs())      b.onActionEnd(tgt, action);
    }

}
