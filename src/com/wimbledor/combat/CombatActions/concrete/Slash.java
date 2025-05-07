// src/com/wimbledor/combat/CombatActions/Slash.java
package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.*;
import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class Slash extends StatScalingAttack {
    // 1) Define this skill’s tuning in a SkillConfig

    private static final SkillConfig CONFIG = new SkillConfig(
            2,                          // basePower
            Map.of(Stat.STRENGTH, 1.0), // scaling: simple STR-based
            5,                          // maxLevel
            20.0,                       // thetaBase
            2.0,                        // deltaTheta
            2.0,                        // softnessExp
            1.0,                        // scaleFactor

            // New fields
            0.1,                        // damageVariance: ±10%
            5,                          // accuracyBonus
            null                        // critChanceOverride: use default
    );

    /** Pass in the user’s current rank of “Slash” (1–5). */
    public Slash(int skillLevel) {
        super(CONFIG, skillLevel, /*forceCrit*/ false);
    }

    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public String getDescription() {
        return "a reliable wing with a blade of some kind. Any idiot could do it";
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
            return actor.getName() + " tried to slash, but missed!";
        }
        return actor.getName() + " swings his sword and slashes " +
                target.getName() + " for " + (amount) +
                (crit ? " (CRITICAL!)" : "");
    }
}
