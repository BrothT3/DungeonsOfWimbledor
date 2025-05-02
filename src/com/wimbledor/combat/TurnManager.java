package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * Schedules and advances turns in a battle, honoring speed order,
 * skipping dead entities, and invoking a callback when the battle ends.
 * Turn pacing is controlled externally via a turnDelayMs variable (delta time).
 */
public class TurnManager {
    /**
     * Milliseconds per turn; UI or game loop should respect this delay between calls.
     */
    private static long turnDelayMs = 500;
    private long timerMs;
    private long accumulator = 0;  // accumulates delta time

    private final List<ICombatEntity> allEntities;
    private ICombatEntity current;
    private final Deque<ICombatEntity> turnQueue = new ArrayDeque<>();

    private final Runnable onBattleOver;

    public TurnManager(ICombatEntity player,
                       List<ICombatEntity> enemies,
                       Runnable onBattleOver) {
        this.allEntities = new ArrayList<>();
        this.allEntities.add(player);
        this.allEntities.addAll(enemies);
        this.onBattleOver = onBattleOver;
        refillQueue();
        this.timerMs = turnDelayMs;
    }
    public void update(long deltaMs) {
        // If battle over, fire callback once
        if (isBattleOver()) {
            onBattleOver.run();
            return;
        }

        // Ensure we have a current actor
        if (current == null) {
            current = nextActor();
            // reset timer for new actor
            timerMs = turnDelayMs;
        }

        // If it's player turn, wait indefinitely for UI action
        if (current.getTeam() == Team.PLAYER) {
            return;
        }

        // Countdown for AI turns
        timerMs -= deltaMs;
        if (timerMs <= 0) {
            // process AI turn
            current.takeTurn(this);
            // clear current so next update picks new actor
            current = null;
        }
    }
    /**
     * Runs the battle loop; should be called repeatedly by external scheduler
     * that respects turnDelayMs between invocations.
     */
    public void startBattle() {
        while (!isBattleOver()) {
            processTurn();
            // pacing handled externally using getTurnDelayMs()
        }
        onBattleOver.run();
    }

    /**
     * Process exactly one turn: select next actor and let them act.
     */
    public void processTurn() {
        ICombatEntity actor = nextActor();
        if (actor == null) return;
        actor.takeTurn(this);
        if (isBattleOver()) onBattleOver.run();
    }

    /**
     * Configurable turn delay in milliseconds; meant for external timing.
     */
    public static void setTurnDelayMs(long ms) {
        turnDelayMs = ms;
    }
    public static long getTurnDelayMs() {
        return turnDelayMs;
    }

    private ICombatEntity nextActor() {
        if (turnQueue.isEmpty()) refillQueue();
        ICombatEntity e = turnQueue.pollFirst();
        if (e == null || !e.isAlive()) {
            return nextActor();
        }
        return e;
    }
    public void playerActionResolved() {
        // clear current and reset timer so AI continues
        current = null;
        timerMs = turnDelayMs;
    }
    private void refillQueue() {
        turnQueue.clear();
        allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(ICombatEntity::getSpeed).reversed())
                .forEach(turnQueue::addLast);
    }

    private boolean isBattleOver() {
        boolean playerAlive = allEntities.stream()
                .filter( e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        boolean enemyAlive  = allEntities.stream()
                .filter(e -> e.getTeam() != Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        return !(playerAlive && enemyAlive);
    }

    public List<ICombatEntity> getEntitiesOnTeam(Team team) {
        List<ICombatEntity> list = new ArrayList<>();
        for (ICombatEntity e : allEntities) {
            if (e.getTeam() == team && e.isAlive()) list.add(e);
        }
        return list;
    }

    public List<ICombatEntity> getEnemiesOf(Team team) {
        List<ICombatEntity> list = new ArrayList<>();
        for (ICombatEntity e : allEntities) {
            if (e.getTeam() != team && e.isAlive()) list.add(e);
        }
        return list;
    }

    public List<ICombatEntity> getAllEntities() {
        List<ICombatEntity> list = new ArrayList<>();
        for (ICombatEntity e : allEntities) {
            if (e.isAlive()) list.add(e);
        }
        return list;
    }
}
