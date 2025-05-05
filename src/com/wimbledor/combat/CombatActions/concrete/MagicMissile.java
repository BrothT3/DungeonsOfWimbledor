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
            /* basePower   */ 4,
            /* statWeights */ Map.of(
            Stat.KNOWLEDGE, 1.0,
            Stat.WILLPOWER, 0.5
    ),
            /* maxLevel     */ 3,
            /* thetaBase    */ 30,
            /* deltaTheta   */ 120,
            /* softnessExp  */ 2,
            /* scaleFactor  */ 1.3,

            // NEW FIELDS
            /* damageVariance */ 0.15,         // +/- 15% variance
            /* accuracyBonus  */ 10,           // +10 accuracy
            /* critChanceOverride */ null      // fixed 10% crit chance
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

    @Override
    public String getDescription() {
        return "a small, condensed chunk of unruly mana can make for quite the pebble to throw";
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
        return actor.getName() + " launches a crackling missile at " +
                target.getName() + ", dealing" + (amount) +
                " damage" + (crit ? " (CRITICAL!)" : "");
    }
}
