package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.CombatMath;
import com.wimbledor.combat.CombatExecutor;
import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

/**
 * Base class for skills scaling with multiple stats.
 * Delegates damage math to CombatMath and hooks into CombatExecutor for execution.
 */
public abstract class StatScalingAttack implements ICombatAction {
    protected final SkillConfig config;
    protected final int skillLevel;
    protected final boolean forceCrit;

    protected StatScalingAttack(
            SkillConfig config,
            int skillLevel,
            boolean forceCrit
    ) {
        this.config     = config;
        this.skillLevel = skillLevel;
        this.forceCrit  = forceCrit;
    }

    @Override
    public void modifyStats(ICombatEntity actor, List<ICombatEntity> targets) {
        // No temporary stat tweaks by default
    }

    @Override
    public void postExecute(
            ICombatEntity actor,
            ICombatEntity target,
            AttackResult result
    ) {
        // Default: no extra behavior. Override for lifesteal, cleave, etc.
    }

    @Override
    public void execute(
            ICombatEntity actor,
            ICombatEntity target
    ) {
        // Delegate entire execution to CombatExecutor
        CombatExecutor.executeSingle(this, actor, target);
    }

    /**
     * Provides the raw scaling roll to CombatMath.
     */
    public CombatMath.AttackRoll roll(
            ICombatEntity actor,
            ICombatEntity target
    ) {
        return CombatMath.rollScaledAttack(this, actor, target);
    }

    // Getters for skill configuration
    public SkillConfig getConfig()     { return config;     }
    public int         getSkillLevel() { return skillLevel; }
    public boolean     isForceCrit()   { return forceCrit;  }

    // Metadata: must implement
    @Override public abstract String getName();
    @Override public abstract TargetMode getTargetMode();

    @Override
    public double getDurationSeconds() {
        // Default durations; override if needed
        return forceCrit ? 0.8 : 0.5;
    }
}
