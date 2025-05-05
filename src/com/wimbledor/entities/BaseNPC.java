// src/com/wimbledor/entities/BaseNPC.java
package com.wimbledor.entities;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.DerivedStatCalculator;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.combat.aiBrains.AiBehavior;
import com.wimbledor.combat.aiBrains.DefaultAiBehavior;
import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for any non-player combatant: monsters, summons, allies, etc.
 * Pure combat logic + flavor hooks + reward stats.
 */
public abstract class BaseNPC implements ICombatEntity {
    private AiBehavior brain;
    private static final AiBehavior DEFAULT_BEHAVIOR = new DefaultAiBehavior();

    public void setBehavior(AiBehavior behavior) {
        this.brain = behavior;
    }

    @Override
    public Decision decideNextAction(List<ICombatEntity> foes) {
        return brain.decide(this, foes);
    }

    private final String name;
    private final Team team;

    private int currentHp;
    private final BaseStats baseStats;

    private final int goldReward;
    private final int expReward;

    private final List<ICombatAction> actions = new ArrayList<>();

    protected BaseNPC(String name,
                      Team team,
                      BaseStats baseStats,
                      int goldReward,
                      int expReward,
                      List<ICombatAction> actions) {
        this.name = name;
        this.team = team;
        this.baseStats = baseStats;
        this.currentHp = getDerived(DerivedStat.MAX_HP);
        this.goldReward = goldReward;
        this.expReward = expReward;
        this.brain = DEFAULT_BEHAVIOR;
        if (actions != null) this.actions.addAll(actions);
    }

    @Override
    public int getStat(Stat s) {
        return switch (s) {
            case STRENGTH -> baseStats.strength;
            case AGILITY -> baseStats.agility;
            case ENDURANCE -> baseStats.endurance;
            case WILLPOWER -> baseStats.willpower;
            case KNOWLEDGE -> baseStats.knowledge;
            case CUNNING -> baseStats.cunning;
        };
    }
    public int getDerived(DerivedStat stat) {
        return DerivedStatCalculator.compute(this, stat);
    }
    public abstract String getRevealText();

    public abstract String getHoverText();

    public int getGoldReward() {
        return goldReward;
    }

    public int getExpReward() {
        return expReward;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Team getTeam() {
        return Team.ENEMY;
    }

    @Override
    public boolean isAlive() {
        return currentHp > 0;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public void applyDamage(int amt) {
        currentHp = Math.max(0, currentHp - amt);
    }

    @Override
    public void heal(int amt) {
        currentHp = Math.min(getDerived(DerivedStat.MAX_HP), currentHp + amt);
    }

    @Override
    public List<ICombatAction> getAvailableActions() {
        return new ArrayList<>(actions);
    }
}
