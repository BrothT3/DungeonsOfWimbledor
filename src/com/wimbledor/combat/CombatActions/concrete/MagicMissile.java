package com.wimbledor.combat.CombatActions.concrete;

import com.wimbledor.combat.*;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatActions.SkillConfig;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;

import java.util.Map;

public class MagicMissile extends StatScalingAttack {
    private static final SkillConfig CONFIG = new SkillConfig(
            /* basePower   */ 40,                // flat starting damage
            /* statWeights */ Map.of(
            Stat.KNOWLEDGE, 1.0,            // primary
            Stat.WILLPOWER, 0.5             // secondary
    ),
            /* maxLevel     */ 3,                // tiers 1–3
            /* thetaBase    */ 30,               // low‐tier softcap
            /* deltaTheta   */ 120,              // extra cap by max tier
            /* softnessExp  */ 2,                // softcap curve
            /* scaleFactor  */ 1.3               // how strongly stats convert to damage
    );

    public MagicMissile() {
        // tier-1 spell, no forced crit
        super(CONFIG, /*skillLevel*/1, /*forceCrit*/false);
    }

    @Override public String getName() {
        return "Magic Missile";
    }

    @Override public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    public static void log(String msg) {
        GameContext.log(msg);
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
            return actor.getName() + " attempted to cast something " +
                    target.getName() + " but the spell fizzled " + actor.getName()+"!";
        }
        return actor.getName() + " condenses a small amount of mana into a maggic missile and shoots it at " +
                target.getName() + ", dealing" + (-amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
