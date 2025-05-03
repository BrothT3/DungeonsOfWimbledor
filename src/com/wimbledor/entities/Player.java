// src/com/wimbledor/entities/Player.java
package com.wimbledor.entities;

import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.effects.Buff;
import com.wimbledor.equipment.Consumable;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.skills.SkillManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private int baseMaxHp, baseAttack, baseDefense, baseDefensePenetration;
    private int baseAccuracy, baseEvasion, baseSpeed;
    private int baseCritChance, baseCritMultiplier;

    private int currentHp;
    private int level, experience, gold;

    private final List<Buff> buffs = new ArrayList<>();
    private final List<Consumable> consumables = new ArrayList<>();

    private final EquipmentManager equipMgr = EquipmentManager.getInstance();
    private final SkillManager skillMgr = SkillManager.getInstance();

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 0;

        this.baseMaxHp = 100;
        this.baseAttack = 10;
        this.baseDefense = 5;
        this.baseDefensePenetration = 0;
        this.baseAccuracy = 80;
        this.baseEvasion = 5;
        this.baseSpeed = 5;
        this.baseCritChance = 10;
        this.baseCritMultiplier = 2;

        this.currentHp = getMaxHp();
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

    @Override
    public List<ICombatAction> getAvailableActions() {
        List<ICombatAction> acts = new ArrayList<>();
        acts.addAll(skillMgr.getSkills(this));
        acts.addAll(equipMgr.getEquipmentActions(this));
        acts.addAll(consumables);
        for (Buff b : buffs) acts.addAll(b.getActions(this));
        return acts;
    }

    @Override
    public void takeTurn(TurnManager tm) {
        List<ICombatAction> acts = getAvailableActions();
        if (acts.isEmpty()) return;
        ICombatAction choice = acts.get(new Random().nextInt(acts.size()));
        List<ICombatEntity> targets = tm.getEnemiesOf(getTeam());
        choice.modifyStats(this, targets);
        choice.execute(this, targets.get(0));
    }

    // === Progression ===
    public void addExperience(int xp) { this.experience += xp; }
    public void addGold(int g)        { this.gold += g; }
    public int getExperience()        { return experience; }
    public int getGold()              { return gold; }
    public int getLevel()             { return level; }

    // Allow modifying base stats (used by items or consumables):
    public void setBaseMaxHp(int baseMaxHp) { this.baseMaxHp = baseMaxHp; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }
    public void setBaseDefense(int baseDefense) { this.baseDefense = baseDefense; }
    public void setBaseDefensePenetration(int baseDefensePenetration) { this.baseDefensePenetration = baseDefensePenetration; }
    public void setBaseAccuracy(int baseAccuracy) { this.baseAccuracy = baseAccuracy; }
    public void setBaseEvasion(int baseEvasion) { this.baseEvasion = baseEvasion; }
    public void setBaseSpeed(int baseSpeed) { this.baseSpeed = baseSpeed; }
    public void setBaseCritChance(int baseCritChance) { this.baseCritChance = baseCritChance; }
    public void setBaseCritMultiplier(int baseCritMultiplier) { this.baseCritMultiplier = baseCritMultiplier; }
    public int getBaseMaxHp()              { return baseMaxHp; }
    public int getBaseAttack()             { return baseAttack; }
    public int getBaseDefense()            { return baseDefense; }
    public int getBaseDefensePenetration() { return baseDefensePenetration; }
    public int getBaseAccuracy()           { return baseAccuracy; }
    public int getBaseEvasion()            { return baseEvasion; }
    public int getBaseSpeed()              { return baseSpeed; }
    public int getBaseCritChance()         { return baseCritChance; }
    public int getBaseCritMultiplier()     { return baseCritMultiplier; }

    // === ICombatEntity ===
    @Override public String getName()          { return name; }
    @Override public Team   getTeam()          { return Team.PLAYER; }
    @Override public boolean isAlive()         { return currentHp > 0; }
    @Override public int     getCurrentHp()    { return currentHp; }
    @Override public int     getMaxHp()        { return baseMaxHp; }
    @Override public void    applyDamage(int a){ currentHp = Math.max(0, currentHp - a); }
    @Override public void    heal(int h)       { currentHp = Math.min(getMaxHp(), currentHp + h); }

    @Override
    public int getAttack() {
        int s = baseAttack + equipMgr.getAttackBonus(this);
        for (Buff b : buffs) s = b.modifyAttack(s);
        return s;
    }
    @Override
    public int getDefense() {
        int s = baseDefense + equipMgr.getDefenseBonus(this);
        for (Buff b : buffs) s = b.modifyDefense(s);
        return s;
    }
    @Override public int getDefensePenetration() {
        return baseDefensePenetration + equipMgr.getPenetrationBonus(this);
    }
    @Override
    public int getAccuracy() {
        int s = baseAccuracy + equipMgr.getAccuracyBonus(this);
        for (Buff b : buffs) s = b.modifyAccuracy(s);
        return s;
    }
    @Override
    public int getEvasion() {
        int s = baseEvasion + equipMgr.getEvasionBonus(this);
        for (Buff b : buffs) s = b.modifyEvasion(s);
        return s;
    }
    @Override
    public int getSpeed() {
        int s = baseSpeed + equipMgr.getSpeedBonus(this);
        for (Buff b : buffs) s = b.modifySpeed(s);
        return s;
    }
    @Override
    public int getCritChance() {
        int s = baseCritChance + equipMgr.getCritChanceBonus(this);
        for (Buff b : buffs) s = b.modifyCritChance(s);
        return s;
    }
    @Override
    public int getCritMultiplier() {
        int s = baseCritMultiplier + equipMgr.getCritDamageBonus(this);
        for (Buff b : buffs) s = b.modifyCritMultiplier(s);
        return s;
    }

    @Override public List<Buff> getBuffs()                { return List.copyOf(buffs); }
    @Override public void addBuff(Buff b)                 { buffs.add(b); b.applyInitial(this); }
    @Override public void removeBuff(Buff b)              { buffs.remove(b); b.remove(this); }
}
