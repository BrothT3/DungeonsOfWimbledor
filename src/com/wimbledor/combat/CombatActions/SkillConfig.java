package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.enums.Stat;

import java.util.Map;

public record SkillConfig(
        int basePower,
        Map<Stat, Double> statWeights,
        int maxLevel,
        double thetaBase,
        double deltaTheta,
        double softnessExp,
        double scaleFactor
) {}
