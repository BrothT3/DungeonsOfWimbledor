package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates a single ICombatAction execution:
 * firing buff hooks, delegating to CombatMath,
 * applying effects, and logging.
 */
public class CombatExecutor {
    /**
     * Execute an action for a list of targets, returning all results.
     */
    public static List<AttackResult> execute(
            ICombatAction action,
            ICombatEntity actor,
            List<ICombatEntity> targets
    ) {
        action.modifyStats(actor, targets);

        List<AttackResult> results = new ArrayList<>();
        for (ICombatEntity target : targets) {
            // 1) Compute the roll
            CombatMath.AttackRoll roll;
            if (action instanceof StatScalingAttack ssa) {
                roll = CombatMath.rollScaledAttack(ssa, actor, target);
            } else {
                roll = CombatMath.rollAttack(actor, target, 1.0, 1.0, false);
            }

            // 2) Apply damage (if any)
            if (roll.hit() && roll.damage() > 0) {
                target.applyDamage(roll.damage());
            }

            // 3) Build the result
            AttackResult result = new AttackResult(
                    action,            // the ICombatAction
                    actor,             // who is attacking
                    target,            // who is being attacked
                    roll.hit(),
                    roll.crit(),
                    roll.damage()
            );

            // 4) Custom hook for additional effects (lifesteal, cleave, etc.)
            action.postExecute(actor, target, result);

            // 5) Log outcome
            GameContext.log(
                    action.getLogMessage(
                            actor, target,
                            result.hit(), result.crit(), result.damage()
                    )
            );

            results.add(result);
        }
        return results;
    }


    /**
     * Convenience for single-target actions.
     */
    public static AttackResult executeSingle(
            ICombatAction action,
            ICombatEntity actor,
            ICombatEntity target
    ) {
        return execute(action, actor, List.of(target)).get(0);
    }
}
