package com.wimbledor.effects;

import com.wimbledor.combat.ICombatAction;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.effects.Buff;

import java.util.List;

/**
 * Represents a buff or debuff with hooks and stat modifiers.
 */
public interface Buff {
    String getName();

    // Stat modifiers before combat
    int modifyMaxHp(int baseMaxHp);
    int modifyAttack(int baseAttack);
    int modifyDefense(int baseDefense);
    int modifyDefensePenetration(int basePenetration);
    int modifyAccuracy(int baseAccuracy);
    int modifyEvasion(int baseEvasion);
    int modifySpeed(int baseSpeed);
    int modifyCritChance(int baseCritChance);
    int modifyCritMultiplier(int baseCritMultiplier);

    // Hooks around attacks and defenses
    void onPreAttack(ICombatEntity attacker, ICombatEntity defender);
    void onPostAttack(ICombatEntity attacker, ICombatEntity defender, boolean hit, boolean crit, int damage);
    void onPreDefend(ICombatEntity defender, ICombatEntity attacker);
    void onPostDefend(ICombatEntity defender, ICombatEntity attacker, boolean hit, boolean crit, int damage);

    // Hooks around any action execution
    void onActionStart(ICombatEntity entity, ICombatAction action);
    void onActionEnd(ICombatEntity entity, ICombatAction action);

    // Initial apply and removal
    void applyInitial(ICombatEntity entity);
    void remove(ICombatEntity entity);

    /**
     * Any additional combat actions granted by this buff on entity's turn.
     */
    List<ICombatAction> getActions(ICombatEntity entity);
}