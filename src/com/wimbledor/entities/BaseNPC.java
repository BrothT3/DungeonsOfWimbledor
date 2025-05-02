// src/com/wimbledor/entities/BaseNPC.java
package com.wimbledor.entities;

import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.effects.Buff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Base class for any non-player combatant: monsters, summons, allies, etc.
 * Pure combat logic + flavor hooks + reward stats.
 */
public abstract class BaseNPC implements ICombatEntity {
    private final String name;
    private final Team team;

    private int currentHp;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final int defensePenetration;
    private final int accuracy;
    private final int evasion;
    private final int speed;
    private final int critChance;
    private final int critMultiplier;

    private final int goldReward;
    private final int expReward;

    private final List<ICombatAction> actions = new ArrayList<>();
    private final List<Buff> buffs = new ArrayList<>();

    protected BaseNPC(String name,
                      Team team,
                      int maxHp,
                      int attack,
                      int defense,
                      int defensePenetration,
                      int accuracy,
                      int evasion,
                      int speed,
                      int critChance,
                      int critMultiplier,
                      int goldReward,
                      int expReward,
                      List<ICombatAction> actions) {
        this.name = name;
        this.team = team;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.defensePenetration = defensePenetration;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.speed = speed;
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
        this.goldReward = goldReward;
        this.expReward = expReward;
        if (actions != null) this.actions.addAll(actions);
    }

    // flavor—implemented by each concrete NPC

    /**
     * Shown once when combat with this NPC starts.
     */
    public abstract String getRevealText();

    /**
     * Shown when hovering over the NPC’s name in the UI.
     */
    public abstract String getHoverText();

    // reward stats
    public int getGoldReward() {
        return goldReward;
    }

    public int getExpReward() {
        return expReward;
    }

    // === ICombatEntity impl ===
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Team getTeam() {
        return team;
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
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public void applyDamage(int amt) {
        currentHp = Math.max(0, currentHp - amt);
    }

    @Override
    public void heal(int amt) {
        currentHp = Math.min(maxHp, currentHp + amt);
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getDefensePenetration() {
        return defensePenetration;
    }

    @Override
    public int getAccuracy() {
        return accuracy;
    }

    @Override
    public int getEvasion() {
        return evasion;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getCritChance() {
        return critChance;
    }

    @Override
    public int getCritMultiplier() {
        return critMultiplier;
    }

    @Override
    public List<Buff> getBuffs() {
        return List.copyOf(buffs);
    }

    @Override
    public void addBuff(Buff buff) {
        buffs.add(buff);
        buff.applyInitial(this);
    }

    @Override
    public void removeBuff(Buff buff) {
        buffs.remove(buff);
        buff.remove(this);
    }

    @Override
    public List<ICombatAction> getAvailableActions(TurnManager tm) {
        return new ArrayList<>(actions);
    }

    @Override
    public void takeTurn(TurnManager tm) {
        List<ICombatEntity> targets = tm.getEnemiesOf(this.getTeam());
        targets.removeIf(e -> !e.isAlive());
        if (actions.isEmpty() || targets.isEmpty()) return;

        ICombatAction choice = actions.get(new Random().nextInt(actions.size()));
        choice.modifyStats(this, targets);
        ICombatEntity target = targets.get(new Random().nextInt(targets.size()));
        choice.execute(this, target);
    }
}
