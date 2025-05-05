package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class FerociousSwing extends StatScalingAttack {


    private static final SkillConfig CONFIG = new SkillConfig(
            /* basePower   */ 50,
            /* statWeights */ Map.of(
            Stat.STRENGTH, 1.0,
            Stat.AGILITY,  0.5
    ),
            /* maxLevel     */ 5,
            /* thetaBase    */  50,
            /* deltaTheta   */ 150,
            /* softnessExp  */   2,
            /* scaleFactor  */  1.2
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
        return actor.getName() + " Lifts their  " +
                target.getName() + " for " + (-amount) +
                (crit ? " (CRITICAL!)" : "");
    }
}
