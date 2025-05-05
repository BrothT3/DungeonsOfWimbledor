package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class Bite extends StatScalingAttack {
    private static final SkillConfig CONFIG = new SkillConfig(
            4,                          // basePower
            Map.of(Stat.STRENGTH, 1.0), // scaling: simple STR-based
            5,                          // maxLevel
            10.0,                       // thetaBase
            2.0,                        // deltaTheta
            2.0,                        // softnessExp
            1.0,                        // scaleFactor

            // New fields
            0.2,                        // damageVariance: ±10%
            0,                          // accuracyBonus
            null                        // critChanceOverride: use default
    );

    public Bite() {
        // tier-2 stealth skill, forceCrit = true for guaranteed crit on hit
        super(CONFIG, /* skillLevel */ 1, /* forceCrit */ true);
    }

    @Override
    public String getName() {
        return "Bite";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public String getDescription() {
        return " utilizing fang as a weapon, through either animalistic ferocity or desperate circumstance";
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
            return actor.getName() + " pounces at "+target.getName()+" but misses it's mark";
        }
        return actor.getName() + " pounces and sinks it's teeth into " +
                target.getName() + "'s flesh, dealing " + (amount) +
                (crit ? " (CRITICAL!)" : "");
    }
}
