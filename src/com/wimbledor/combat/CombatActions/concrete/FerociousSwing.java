package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class FerociousSwing extends StatScalingAttack {



    private static final SkillConfig CONFIG = new SkillConfig(
            6,                                  // basePower
            Map.of(Stat.STRENGTH, 0.7, Stat.ENDURANCE, 0.3), // scaling
            4,                                  // maxLevel
            12.0,                               // thetaBase
            2.0,                                // deltaTheta
            2.5,                                // softnessExp
            1.1,                                // scaleFactor

            // New fields
            0.4,                                // damageVariance: ±20%
            -10,                                 // accuracyBonus: slightly harder to land
            null                                // critChanceOverride
    );

    public FerociousSwing(int skillLevel) {
        super(CONFIG, skillLevel, /*forceCrit*/ false);
    }

    @Override
    public String getName() {
        return "Ferocious Swing";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public String getDescription() {
        return "it's when you forget your own safety, that that of your enemies is most at risk ";
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
            return actor.getName() + " misses "+target.getName()+" with a fercious swing!";
        }
        return actor.getName() + " swings fiercely at " +
                target.getName() + " without restraint! dealing " + (amount) +
                (crit ? " (CRITICAL!)" : "");
    }
}
