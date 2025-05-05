// src/com/wimbledor/combat/CombatMath.java
package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;
import java.util.Random;

/**
 * Pure, side-effect-free combat calculations:
 * hit chance, crit, damage, soft-cap, multi-stat scaling.
 */
public class CombatMath {
    private static final Random RNG = new Random();

    /**
     * Result of raw combat roll: hit, crit, damage.
     */
    public record AttackRoll(boolean hit, boolean crit, int damage) {}

    /**
     * Multi-stat soft-capped scaling roll for skills.
     */
    public static AttackRoll rollScaledAttack(
            StatScalingAttack ssa,
            ICombatEntity attacker,
            ICombatEntity defender
    ) {
        SkillConfig cfg = ssa.getConfig();
        int lvl = ssa.getSkillLevel();
        boolean forceCrit = ssa.isForceCrit();

        // 1. Compute scaling cap threshold (theta)
        double theta = cfg.thetaBase()
                + cfg.deltaTheta() * ((double) lvl / cfg.maxLevel());

        // 2. Soft-capped weighted stat sum
        double sum = 0;
        for (Map.Entry<Stat, Double> entry : cfg.statWeights().entrySet()) {
            int statVal = attacker.getStat(entry.getKey());
            double capped = statVal / Math.pow(1 + Math.pow(statVal / theta, cfg.softnessExp()), 1.0 / cfg.softnessExp());
            sum += entry.getValue() * capped;
        }

        // 3. Base power scaling
        double baseDamage = cfg.basePower() + cfg.scaleFactor() * sum;

        // 4. Accuracy roll (unless forced hit)
        boolean hit = forceCrit || CombatMath.rollHit(attacker, defender, cfg.accuracyBonus());

        // 5. Crit roll (or forced crit)
        boolean crit = forceCrit || CombatMath.rollCrit(attacker, cfg.critChanceOverride());

        // 6. Final damage calculation (if hit)
        int damage = 0;
        if (hit) {
            double varied = CombatMath.applyVariance(baseDamage, cfg.damageVariance());
            int critMultiplier = DerivedStatCalculator.compute(attacker, DerivedStat.CRIT_MULTIPLIER);
            damage = (int) (crit ? varied * critMultiplier : varied);
        }

        return new AttackRoll(hit, crit, damage);
    }


    public static boolean rollHit(ICombatEntity attacker, ICombatEntity defender, int bonusAccuracy) {
        int accuracy = DerivedStatCalculator.compute(attacker, DerivedStat.ACCURACY) + bonusAccuracy;
        int evasion = DerivedStatCalculator.compute(defender, DerivedStat.EVASION);

        int hitChance = accuracy - evasion;
        hitChance = Math.max(5, Math.min(95, hitChance));
        return RNG.nextInt(100) < hitChance;
    }

    /**
     * Rolls whether an attack crits based on attacker crit chance or an override.
     */
    public static boolean rollCrit(ICombatEntity attacker, Integer overrideCritChance) {
        int critChance = (overrideCritChance != null)
                ? overrideCritChance
                : DerivedStatCalculator.compute(attacker, DerivedStat.CRIT_CHANCE);

        return RNG.nextInt(100) < critChance;
    }

    /**
     * Applies variance to raw damage output. The value returned is scaled randomly within ±variance%.
     */
    public static double applyVariance(double base, double variancePercent) {
        double min = base * (1.0 - variancePercent);
        double max = base * (1.0 + variancePercent);
        return min + (max - min) * RNG.nextDouble();
    }
}

