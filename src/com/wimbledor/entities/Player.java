// src/com/wimbledor/entities/Player.java
package com.wimbledor.entities;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.DerivedStatCalculator;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.equipment.Consumable;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.skills.SkillManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Full ICombatEntity for the player, with:
 * - Base stats
 * - Equipment bonuses
 * - Consumables & skills as actions
 * - Buffs/debuffs
 * - Experience & gold
 */
public class Player implements ICombatEntity {
    private final String name;
    private final BaseStats baseStats;

    private int currentHp;
    private final int level;
    private int experience;
    private int gold;
    private final Team team;
    private final List<Consumable> consumables = new ArrayList<>();

    private final EquipmentManager equipMgr = EquipmentManager.getInstance();
    private final SkillManager skillMgr = SkillManager.getInstance();

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 0;
        this.baseStats = new BaseStats(10, 10, 100, 10, 10, 10);
        this.currentHp = getDerived(DerivedStat.MAX_HP);
        team = Team.PLAYER;
    }

    // === Actions ===
    public void addConsumable(Consumable c) {
        if (consumables.size() < 3) consumables.add(c);
    }

    public void useConsumable(int slot) {
        if (slot >= 0 && slot < consumables.size()) {
            Consumable c = consumables.remove(slot);
            c.applyEffect(this);
        }
    }

    public void removeConsumable(Consumable c) {
        consumables.remove(c);
    }

    public List<Consumable> getConsumables() {
        return List.copyOf(consumables);
    }

    public List<ICombatAction> getTempConAct() {
        List<ICombatAction> actions = new ArrayList<>();
        for (Consumable c : consumables) {
            actions.add(new ICombatAction() {
                @Override
                public String getName() {
                    return c.getName();
                }

                @Override
                public TargetMode getTargetMode() {
                    return TargetMode.SELF;
                }

                @Override
                public void modifyStats(ICombatEntity actor, List<ICombatEntity> targets) {
                    // no-op for now
                }

                @Override
                public void execute(ICombatEntity actor, ICombatEntity target) {
                    c.applyEffect(actor);
                }

                @Override
                public String getDescription() {
                    return "TEMP: uses " + c.getClass().getSimpleName();
                }

                @Override
                public double getDurationSeconds() {
                    return 0.5;
                }

                public String getLogMessage() {
                    return getName() + " used a " + c.getName();
                }
            });
        }
        return actions;
    }

    @Override
    public List<ICombatAction> getAvailableActions() {
        List<ICombatAction> acts = new ArrayList<>();
        acts.addAll(skillMgr.getSkills(this));
        acts.addAll(equipMgr.getEquipmentActions(this));
        acts.addAll(consumables);
        return acts;
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

    @Override
    public int getDerived(DerivedStat stat) {
        return DerivedStatCalculator.compute(this, stat);
    }

    // === Progression ===
    public void addExperience(int xp) {
        this.experience += xp;
    }

    public void addGold(int g) {
        this.gold += g;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    public int getLevel() {
        return level;
    }

    // === ICombatEntity ===
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Decision decideNextAction(List<ICombatEntity> foes) {
        return null;
    }

    @Override
    public Team getTeam() {
        return Team.PLAYER;
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
    public void applyDamage(int a) {
        currentHp = Math.max(0, currentHp - a);
    }

    @Override
    public void heal(int h) {
        currentHp = Math.min(getDerived(DerivedStat.MAX_HP), currentHp + h);
    }
}
