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

    public RoundTurnQueue(List<ICombatEntity> sortedEntities) {
        Objects.requireNonNull(sortedEntities);
        currentRound.addAll(sortedEntities);

        System.out.println("=== New Round ===");
        sortedEntities.forEach(e -> System.out.println(" - " + e.getName() + " (" + e.getTeam() + ")"));
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
