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
            /* basePower   */ 30,             // light starting damage
            /* statWeights */ Map.of(
            Stat.CUNNING, 0.85,            // primary
            Stat.AGILITY, 0.60             // secondary
    ),
            /* maxLevel     */ 4,             // tiers 1–4
            /* thetaBase    */ 40,            // low‐tier softcap
            /* deltaTheta   */ 160,           // extra cap by max tier
            /* softnessExp  */ 2,             // softcap curve
            /* scaleFactor  */ 1.4            // how strongly stats convert to damage
    );

    public Backstab() {
        // tier-2 stealth skill, forceCrit=true for guaranteed crit on hit
        super(CONFIG, /*skillLevel*/2, /*forceCrit*/true);
    }

    @Override public String getName() {
        return "Backstab";
    }

    @Override public TargetMode getTargetMode() {
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
            return actor.getName() + " tried to fade from view, but " +
                    target.getName() + " saw the " + actor.getName()+"!";
        }
        return actor.getName() + " saw an opportunity and struck " +
                target.getName() + ", backstabbing them for " + (-amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
