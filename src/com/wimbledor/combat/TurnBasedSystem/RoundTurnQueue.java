package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.*;

/**
 * Manages per-round turn queue for combat. Sorts alive entities by speed at the start of each round.
 */
public class RoundTurnQueue {
    private final Deque<ICombatEntity> currentRound = new ArrayDeque<>();

    public RoundTurnQueue(List<ICombatEntity> sortedEntities, int roundNumber) {
        Objects.requireNonNull(sortedEntities);
        currentRound.addAll(sortedEntities);

        System.out.println("=== Round "+roundNumber+" ===");
        sortedEntities.forEach(e -> System.out.println(" - " + e.getName() + " (" + e.getTeam() + ")"));
    }
    /** NEW: remove a just-killed actor from the remainder of this round. */
    public void removeDead(ICombatEntity dead) {
        currentRound.removeIf(e -> e.equals(dead));
    }

    /** NEW: snapshot of whoever’s still in the queue, for UI display. */
    public List<ICombatEntity> upcoming() {
        return List.copyOf(currentRound);
    }
    public void setCurrentRound(int i){

    }
    public ICombatEntity nextTurn() {
        while (!currentRound.isEmpty()) {
            ICombatEntity next = currentRound.pollFirst();
            if (next != null && next.isAlive()) return next;
        }
        return null; // round exhausted
    }

    public ICombatEntity peekNext() {
        return currentRound.peekFirst();
    }

    public boolean isExhausted() {
        return currentRound.isEmpty();
    }
}
