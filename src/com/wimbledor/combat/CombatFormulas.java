// src/com/wimbledor/combat/CombatFormulas.java
package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;

public class CombatFormulas {

    /** Max HP might be Endurance × 10 + Strength × 2. */
    public static int calcMaxHp(ICombatEntity e) {
        return e.getEndurance() * 10 + e.getStrength() * 2;
    }

    /** Accuracy as a percentage: e.g. Agility×2 + Cunning×1. */
    public static double calcAccuracy(ICombatEntity e) {
        return e.getAgility() * 2.0 + e.getCunning() * 1.0;
    }

    /** Evasion: e.g. Agility×1 + Cunning×0.5. */
    public static double calcEvasion(ICombatEntity e) {
        return e.getAgility() * 1.0 + e.getCunning() * 0.5;
    }

    /** Defense: maybe Endurance×1 + Strength×0.5. */
    public static int calcDefense(ICombatEntity e) {
        return e.getEndurance() + (int)(e.getStrength() * 0.5);
    }

    /** Penetration: Cunning×1 + Agility×0.2, for instance. */
    public static int calcPenetration(ICombatEntity e) {
        return e.getCunning() + (int)(e.getAgility() * 0.2);
    }

    /** Crit chance: Cunning×0.5 + Agility×0.2, capped at 100. */
    public static double calcCritChance(ICombatEntity e) {
        double c = e.getCunning() * 0.5 + e.getAgility() * 0.2;
        return Math.min(100, c);
    }

    /** Crit multiplier: Strength×0.1 + 1.5 base, etc. */
    public static double calcCritMultiplier(ICombatEntity e) {
        return 1.5 + e.getStrength() * 0.1;
    }

    /** Base damage before defense: Strength×2 + Agility×0.5. */
    public static int calcRawDamage(ICombatEntity e) {
        return (int)(e.getStrength() * 2.0 + e.getAgility() * 0.5);
    }

    // …and so on for healing, dot potency (Willpower, Knowledge), buff values, etc.
}
