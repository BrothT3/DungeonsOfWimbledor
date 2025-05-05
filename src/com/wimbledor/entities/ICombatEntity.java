// src/com/wimbledor/entities/ICombatEntity.java
package com.wimbledor.entities;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;

import java.util.List;

/**
 * Any combatant: player, ally, or monster.
 * All core stats are exposed, including defense penetration,
 * and buff/debuff support.
 */
public interface ICombatEntity {
    int getDerived(DerivedStat stat);

    /**
     * Unique name for logs/UI
     */
    String getName();
    Decision decideNextAction(List<ICombatEntity> foes);

    Team getTeam();

    boolean isAlive();

    int getCurrentHp();


    void applyDamage(int amount);

    void heal(int amount);

    /**
     * Core combat stats (base + equipment + buffs)
     */


    /**
     * All actions available this turn.
     * Player → gear, skills, consumables, buffs.
     * Monster → its predefined card actions.
     */
    List<ICombatAction> getAvailableActions();

    /**
     * Called by TurnManager when it’s this entity’s turn.
     */

    /**
     * Buff/debuff management
     */

    int getStat(Stat s);

}

