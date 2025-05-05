package com.wimbledor.effects;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

public interface StatusEffect {
    enum Scope{COMBAT, ENCOUNTER}
    Scope getScope();

    int getDuration();
    void decrementDuration();

    default void onApply(ICombatEntity target) {}

    default void onCombatTurnStart(ICombatEntity target) {}

    default void onCombatTurnEnd(ICombatEntity target) {}
    default void onExpire(ICombatEntity target) {}
    default void onEncounterBegin(ICombatEntity target) {}

    default void onActionTick(
            ICombatEntity actor,
            ICombatAction action,
            boolean hit,
            boolean crit,
            int damage
    ) {}
    default void onEncounterTick() {}
    default void onActionStart(ICombatEntity actor, ICombatAction action) {}
    default int modifyGoldReward(int baseGold) { return baseGold; }
    default int modifyXpReward(int baseXp)   { return baseXp; }
}
