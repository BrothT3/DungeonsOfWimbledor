// src/com/wimbledor/entities/ICombatEntity.java
package com.wimbledor.entities;

import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.effects.Buff;

import java.util.List;

/**
 * Any combatant: player, ally, or monster.
 * All core stats are exposed, including defense penetration,
 * and buff/debuff support.
 */
public interface ICombatEntity {
    /**
     * Unique name for logs/UI
     */
    String getName();


    Team getTeam();

    boolean isAlive();

    int getCurrentHp();

    int getMaxHp();

    void applyDamage(int amount);

    void heal(int amount);

    /**
     * Core combat stats (base + equipment + buffs)
     */
    int getAttack();

    int getDefense();

    int getDefensePenetration();

    int getAccuracy();

    int getEvasion();

    int getSpeed();

    int getCritChance();

    int getCritMultiplier();

    /**
     * All actions available this turn.
     * Player → gear, skills, consumables, buffs.
     * Monster → its predefined card actions.
     */
    List<ICombatAction> getAvailableActions();

    /**
     * Called by TurnManager when it’s this entity’s turn.
     */
    void takeTurn(TurnManager tm);

    /**
     * Buff/debuff management
     */
    List<Buff> getBuffs();

    void addBuff(Buff buff);

    void removeBuff(Buff buff);

    int getStrength();
    int getAgility();
    int getEndurance();
    int getWillpower();
    int getKnowledge();
    int getCunning();
}

