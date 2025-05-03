// src/com/wimbledor/combat/TurnManager.java
package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TurnManager {
    private final List<ICombatEntity> allEntities;
    private final Deque<ICombatEntity> turnQueue = new ArrayDeque<>();
    private final Runnable            onBattleOver;

    public TurnManager(ICombatEntity player,
                       List<ICombatEntity> enemies,
                       Runnable onBattleOver) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(enemies, "enemies");
        this.onBattleOver = Objects.requireNonNull(onBattleOver, "onBattleOver");

        this.allEntities = new ArrayList<>();
        this.allEntities.add(player);
        this.allEntities.addAll(enemies);
        refillQueue();
    }

    /** Sorts alive combatants by speed descending into the queue. */
    private void refillQueue() {
        turnQueue.clear();
        allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(ICombatEntity::getSpeed).reversed())
                .forEach(turnQueue::addLast);
    }

    /**
     * Run exactly one turn. Returns the entity who just ACTED,
     * or null if the battle is over.
     *  - If it's the player, no action is executed; you must call playerResolved().
     *  - If it's AI, we immediately call its takeTurn().
     */
    public ICombatEntity processNextTurn() {
        if (isBattleOver()) {
            onBattleOver.run();
            return null;
        }
        if (turnQueue.isEmpty()) refillQueue();

        ICombatEntity actor = turnQueue.pollFirst();
        if (actor == null || !actor.isAlive()) {
            return processNextTurn(); // skip dead
        }

        if (actor.getTeam() == Team.PLAYER) {
            // pause here; UI must call playerResolved() next
            // re‐enqueue the player at the front so that processNextTurn()
            // continues to return them until they resolve.
            return actor;
        }

        // AI's turn: execute immediately
        actor.takeTurn(this);
        turnQueue.addLast((actor));
        return actor;
    }

    /** Call this _once_ after you've executed the player’s chosen action. */
    public void playerResolved() {
        ICombatEntity player = getPlayerEntity();
        // remove that one instance from the queue and put them at the back
        if (player.isAlive()) {
            turnQueue.addLast(player);
        }
    }

    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        boolean anyEnemy  = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        return !(anyPlayer && anyEnemy);
    }

    public ICombatEntity getPlayerEntity() {
        return allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player in battle"));
    }

    public List<ICombatEntity> getPlayers() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .filter(e -> e.getTeam() == Team.PLAYER)
                .collect(Collectors.toList());
    }

    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .filter(e -> e.getTeam() != team)
                .collect(Collectors.toList());
    }
}
