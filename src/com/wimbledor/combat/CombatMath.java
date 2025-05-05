// src/com/wimbledor/combat/CombatMath.java
package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.CombatActions.SkillConfig;
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
     * Basic damage roll using attack, defense, penetration, crit, etc.
     */
    public static AttackRoll rollAttack(
            ICombatEntity attacker,
            ICombatEntity defender,
            double accuracyMultiplier,
            double damageMultiplier,
            boolean forceCrit
    ) {
        double accRoll = attacker.getAccuracy() * accuracyMultiplier;
        boolean hit = RNG.nextDouble() * 100 < (accRoll - defender.getEvasion());
        boolean crit = false;
        if (hit) {
            crit = forceCrit || RNG.nextDouble() < attacker.getCritChance() / 100.0;
        }
        int attackValue = attacker.getAttack();
        int penetration = attacker.getDefensePenetration();

        int penPart = Math.min(penetration, attackValue);
        int nonPen = attackValue - penPart;
        int effective = Math.max(0, nonPen - defender.getDefense());

        int raw = penPart + effective;
        int scaled = (int)(raw * damageMultiplier);
        int damage = crit
                ? scaled * attacker.getCritMultiplier()
                : scaled;

        return new AttackRoll(hit, crit, damage);
    }

    /**
     * Multi-stat soft-capped scaling roll for skills.
     */
    public static AttackRoll rollScaledAttack(
            StatScalingAttack ssa,
            ICombatEntity     attacker,
            ICombatEntity     defender
    ) {
        SkillConfig cfg = ssa.getConfig();
        int lvl = ssa.getSkillLevel();
        boolean force = ssa.isForceCrit();

        double theta = cfg.thetaBase()
                + cfg.deltaTheta() * ((double)lvl / cfg.maxLevel());

        double sum = 0;
        for (Map.Entry<Stat, Double> entry : cfg.statWeights().entrySet()) {
            double rawStat = getStat(attacker, entry.getKey());
            double capped = rawStat / Math.pow(1 + Math.pow(rawStat / theta, cfg.softnessExp()), 1.0/cfg.softnessExp());
            sum += entry.getValue() * capped;
        }

        double preCrit = cfg.basePower() + cfg.scaleFactor() * sum;
        AttackRoll base = rollAttack(attacker, defender, 1.0, 1.0, force);

        boolean hit = base.hit();
        boolean crit = base.crit();
        int damage = 0;
        if (hit) {
            damage = crit
                    ? (int)(preCrit * attacker.getCritMultiplier())
                    : (int)preCrit;
        }
        return new AttackRoll(hit, crit, damage);
    }

    /**
     * Retrieve the value of a given base stat from an entity.
     */
    public static int getStat(ICombatEntity e, Stat stat) {
        return switch (stat) {
            case STRENGTH  -> e.getStrength();
            case AGILITY   -> e.getAgility();
            case ENDURANCE -> e.getEndurance();
            case WILLPOWER -> e.getWillpower();
            case KNOWLEDGE -> e.getKnowledge();
            case CUNNING   -> e.getCunning();
        };
    }
}