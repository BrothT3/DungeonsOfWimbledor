package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Schedules and advances turns, with explicit support for
 * waiting on player input (via playerActionResolved()).
 */
public class TurnManager {
    private final List<ICombatEntity> allEntities;
    private final Deque<ICombatEntity> turnQueue = new ArrayDeque<>();
    private final Runnable onBattleOver;
    public static long TURN_DELAY_MS = 500;


    // state of whose turn it is
    private ICombatEntity currentEntity;

    public TurnManager(ICombatEntity player,
                       List<ICombatEntity> enemies,
                       Runnable onBattleOver) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(enemies, "enemies");
        this.onBattleOver = Objects.requireNonNull(onBattleOver, "onBattleOver");

        this.allEntities = new ArrayList<>();
        this.allEntities.add(player);
        this.allEntities.addAll(enemies);

        refillQueue();  // prepare the first round
    }
    private void refillQueue() {
        turnQueue.clear();
        allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(ICombatEntity::getSpeed).reversed())
                .forEach(turnQueue::addLast);
    }

    /**
     * Called each tick by GameLoop.
     * If it's AI's turn, processes exactly one turn.
     * If it's the player's turn, returns immediately and waits
     * for playerActionResolved().
     * If battle is over, fires onBattleOver once.
     */
    public void update(long deltaMs) {
        if (isBattleOver()) {
            onBattleOver.run();
            return;
        }

        // If we ran out of this round's queue, start a fresh round
        if (turnQueue.isEmpty()) {
            refillQueue();
        }

        // If no one has been chosen yet, pick the next
        if (currentEntity == null) {
            currentEntity = turnQueue.pollFirst();
        }

        // If it's still null (all dead?), bail
        if (currentEntity == null) {
            onBattleOver.run();
            return;
        }

        // If it's the player's turn, pause here until playerActionResolved()
        if (currentEntity.getTeam() == Team.PLAYER) {
            return;
        }

        // Otherwise, it's an AI turn: do exactly one takeTurn
        currentEntity.takeTurn(this);
        // and advance currentEntity so next tick picks the next combatant
        currentEntity = null;
    }

    /** Called by UI when the player has selected & executed their action. */
    public void playerActionResolved() {
        if (currentEntity != null && currentEntity.getTeam() == Team.PLAYER) {
            currentEntity = null;
        }
    }

    /** Picks the next living entity by speed descending. */
    private void startNextTurn() {
        // filter, sort, and pick the highest-speed alive combatant
        currentEntity = allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .max(Comparator.comparingInt(ICombatEntity::getSpeed))
                .orElse(null);
        // (should never be null here unless all dead)
    }

    public ICombatEntity getCurrentEntity() {
        return currentEntity;
    }

    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        boolean anyEnemy = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        return !(anyPlayer && anyEnemy);
    }

    /** Returns the one player in this fight. */
    public ICombatEntity getPlayerEntity() {
        return allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player in battle"));
    }

    /** Returns all living entities not on the given team. */
    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .filter(e -> e.getTeam() != team)
                .collect(Collectors.toList());
    }
    public static long getTurnDelayMs() {
        return TURN_DELAY_MS;
    }
    /** Returns all living combatants. */
    public List<ICombatEntity> getAllEntities() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .collect(Collectors.toList());
    }
}
