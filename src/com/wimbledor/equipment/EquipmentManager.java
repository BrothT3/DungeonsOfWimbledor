// src/com/wimbledor/equipment/EquipmentManager.java
package com.wimbledor.equipment;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.engine.GameContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton managing equipped gear, stat bonuses, and granted combat actions.
 */
public class EquipmentManager {
    private static EquipmentManager instance;
    private IEquipment equippedWeapon;
    private IEquipment equippedArmor;
    private IEquipment equippedAccessory;

    private EquipmentManager() {}

    public static EquipmentManager getInstance() {
        if (instance == null) instance = new EquipmentManager();
        return instance;
    }

    // Simply change what is equipped; no immediate side-effects
    public void equipWeapon(IEquipment w)   { equippedWeapon = w; }
    public void equipArmor(IEquipment a)    { equippedArmor  = a; }
    public void equipAccessory(IEquipment a){ equippedAccessory = a; }

    // Dynamic stat getters
    public int getAttackBonus(ICombatEntity e)        { return equippedWeapon   != null ? equippedWeapon.getAttackBonus()   : 0; }
    public int getDefenseBonus(ICombatEntity e)       { return equippedArmor    != null ? equippedArmor.getDefenseBonus()    : 0; }
    public int getPenetrationBonus(ICombatEntity e)   { return equippedWeapon   != null ? equippedWeapon.getPenetrationBonus(): 0; }
    public int getAccuracyBonus(ICombatEntity e)      { return equippedWeapon   != null ? equippedWeapon.getAccuracyBonus()   : 0; }
    public int getEvasionBonus(ICombatEntity e)       { return equippedArmor    != null ? equippedArmor.getEvasionBonus()    : 0; }
    public int getSpeedBonus(ICombatEntity e)         {
        int b = 0;
        if (equippedWeapon   != null) b += equippedWeapon.getSpeedBonus();
        if (equippedArmor    != null) b += equippedArmor.getSpeedBonus();
        if (equippedAccessory!= null) b += equippedAccessory.getSpeedBonus();
        return b;
    }
    public int getCritChanceBonus(ICombatEntity e)    { return equippedAccessory!= null ? equippedAccessory.getCritChanceBonus() : 0; }
    public int getCritDamageBonus(ICombatEntity e)    { return equippedAccessory!= null ? equippedAccessory.getCritDamageBonus(): 0; }

    /**
     * Gather every ICombatAction that gear grants.
     */
    public List<ICombatAction> getEquipmentActions(ICombatEntity actor) {
        List<ICombatAction> acts = new ArrayList<>();
        if (equippedWeapon    != null) acts.addAll(equippedWeapon.getActions());
        if (equippedArmor     != null) acts.addAll(equippedArmor.getActions());
        if (equippedAccessory != null) acts.addAll(equippedAccessory.getActions());
        return Collections.unmodifiableList(acts);
    }

    // Shortcuts to manage consumables on the singleton player
    public void addConsumable(Consumable c) { GameContext.getPlayer().addConsumable(c); }
    public void useConsumable(int slot)    { GameContext.getPlayer().useConsumable(slot); }

    public IEquipment getEquippedWeapon() {
        return equippedWeapon;
    }

    public IEquipment getEquippedArmor() {
        return equippedArmor;
    }

    public IEquipment getEquippedAccessory() {
        return equippedAccessory;
    }
}
