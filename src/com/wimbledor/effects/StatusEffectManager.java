package com.wimbledor.effects;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusEffectManager {
    private final Map<ICombatEntity, List<StatusEffect>> combatEffects = new HashMap<>();
    private final List<StatusEffect> globalEffects = new ArrayList<>();

    public void addEffect(ICombatEntity target, StatusEffect effect) {
        if (effect.getScope() == StatusEffect.Scope.COMBAT) {
            // Look up the list for this entity
            List<StatusEffect> list = combatEffects.get(target);
            // If there isn't one yet, create it and put it in the map
            if (list == null) {
                list = new ArrayList<>();
                combatEffects.put(target, list);
            }
            // Now add the effect
            list.add(effect);
        } else {
            // Global effects just go in a flat list
            globalEffects.add(effect);
        }
        // Let the effect do any “on apply” work
        effect.onApply(target);
    }

    public void removeEffect(ICombatEntity target, StatusEffect effect) {
        if (effect.getScope() == StatusEffect.Scope.COMBAT) {
            // Find the list for this entity
            List<StatusEffect> list = combatEffects.get(target);
            if (list != null) {
                // Remove the effect
                list.remove(effect);
                // If no more effects remain, clean up the empty list
                if (list.isEmpty()) {
                    combatEffects.remove(target);
                }
            }
        } else {
            // Simply remove from the global list
            globalEffects.remove(effect);
        }
        // Let the effect do any “on expire” work
        effect.onExpire(target);
    }

    public List<StatusEffect> getCombatEffects(ICombatEntity e) {
        return combatEffects.getOrDefault(e, List.of());
    }

    public List<StatusEffect> getGlobalEffects() {
        return List.copyOf(globalEffects);
    }

    public void tickCombatTurnStart(ICombatEntity entity) {
        // 1) Get the list of effects on this entity
        List<StatusEffect> list = combatEffects.get(entity);
        if (list == null) return;

        // 2) We’ll collect expired effects so we can remove them after iterating
        List<StatusEffect> toRemove = new ArrayList<>();

        // 3) For each effect:
        for (StatusEffect effect : list) {
            // A) Run its turn-start hook
            effect.onCombatTurnStart(entity);

            // B) Decrease its remaining duration
            effect.decrementDuration();

            // C) If it’s expired, mark it
            if (effect.getDuration() <= 0) {
                toRemove.add(effect);
            }
        }

        // 4) Clean up all expired effects
        for (StatusEffect expired : toRemove) {
            removeEffect(entity, expired);
        }
    }

    public void tickCombatTurnEnd(
            ICombatEntity actor,
            ICombatAction action,
            boolean hit,
            boolean crit,
            int damage
    ) {
        List<StatusEffect> list = combatEffects.get(actor);
        if (list == null) return;

        List<StatusEffect> expired = new ArrayList<>();
        for (StatusEffect se : list) {
            se.onActionTick(actor, action, hit, crit, damage);
            se.decrementDuration();
            if (se.getDuration() <= 0) expired.add(se);
        }
        for (StatusEffect se : expired) removeEffect(actor, se);
    }

    public void clearAllCombatEffects() {
        for (ICombatEntity e : new ArrayList<>(combatEffects.keySet())) {
            for (StatusEffect se : new ArrayList<>(combatEffects.get(e))) {
                removeEffect(e, se);
            }
        }
        combatEffects.clear();
    }

    public void tickEncounter() {
        List<StatusEffect> expired = new ArrayList<>();
        for (StatusEffect se : globalEffects) {
            se.decrementDuration();
            if (se.getDuration() <= 0) expired.add(se);
        }
        for (StatusEffect se : expired) {
            removeEffect(null, se);
        }
    }
}


