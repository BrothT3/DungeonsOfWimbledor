// src/com/wimbledor/combat/CombatActions/Backstab.java
package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class Backstab extends StatScalingAttack {
    private static final SkillConfig CONFIG = new SkillConfig(
            /* basePower          */ 1,
            /* statWeights        */ Map.of(
            Stat.CUNNING, 0.85,
            Stat.AGILITY, 0.60
    ),
            /* maxLevel           */ 4,
            /* thetaBase          */ 20,
            /* deltaTheta         */ 160,
            /* softnessExp        */ 2,
            /* scaleFactor        */ 1.4,

            // New fields:
            /* damageVariance     */ 0.25,      // Slightly more volatile
            /* accuracyBonus      */ -35,        // More likely to hit from stealth
            /* critChanceOverride */ null       // Use default crit chance (but forced in logic)
    );

    public Backstab() {
        // tier-2 stealth skill, forceCrit = true for guaranteed crit on hit
        super(CONFIG, /* skillLevel */ 2, /* forceCrit */ true);
    }

    @Override
    public String getName() {
        return "Backstab";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public String getDescription() {
        return "Getting close enough to deftly thrust a blade between the ribs is much too difficult in a real fight. If you're seen, that is.";
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
            return actor.getName() + " tried to fade from view, but " +
                    target.getName() + " saw them!";
        }
        return actor.getName() + " saw an opportunity and struck " +
                target.getName() + ", backstabbing them for " + (amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
