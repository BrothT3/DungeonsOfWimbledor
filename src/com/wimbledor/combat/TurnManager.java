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

        // build the combatants list
        this.allEntities = new ArrayList<>();
        this.allEntities.add(player);
        this.allEntities.addAll(enemies);

        // no one has acted yet
        this.currentEntity = null;
    }

    /**
     * Called each tick by GameLoop.
     * If it's AI's turn, processes exactly one turn.
     * If it's the player's turn, returns immediately and waits
     * for playerActionResolved().
     * If battle is over, fires onBattleOver once.
     */
    public void update(long deltaMs) {
        // If the fight has already ended, fire callback (once).
        if (isBattleOver()) {
            onBattleOver.run();
            return;
        }

        // If no current actor, pick next
        if (currentEntity == null) {
            startNextTurn();
        }

        // If it's the player, wait for playerActionResolved()
        if (currentEntity.getTeam() == Team.PLAYER) {
            return;
        }

        // Otherwise: AI actor takes exactly one turn
        currentEntity.takeTurn(this);
        // clear so that next update() will pick the next
        currentEntity = null;
    }

    /** Called by UI when the player has selected & executed their action. */
    public void playerActionResolved() {
        if (isBattleOver()) {
            onBattleOver.run();
            return;
        }
        // if we were waiting on the player, let them act now:
        if (currentEntity != null && currentEntity.getTeam() == Team.PLAYER) {
            // We assume the UI has already applied the action,
            // so we simply clear current so update() can advance.
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
