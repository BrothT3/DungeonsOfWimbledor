// src/com/wimbledor/combat/TurnManager.java
package com.wimbledor.combat;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Pull‐based, speed‐queued turn scheduler.
 * No callbacks—CardController.resolve(...) handles end‐of‐battle transitions.
 */
public class TurnManager {
    private final List<ICombatEntity> allEntities;
    private final Deque<ICombatEntity> turnQueue = new ArrayDeque<>();

    /**
     * @param player  the player combatant
     * @param enemies list of enemy combatants
     */
    public TurnManager(ICombatEntity player, List<ICombatEntity> enemies) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(enemies, "enemies");

        this.allEntities = new ArrayList<>();
        this.allEntities.add(player);
        this.allEntities.addAll(enemies);
        refillQueue();
    }

    /** Sorts alive combatants by descending speed into the queue. */
    private void refillQueue() {
        turnQueue.clear();
        allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(ICombatEntity::getSpeed).reversed())
                .forEach(turnQueue::addLast);
    }

    /**
     * Process exactly one turn.
     * @return the actor who must be resolved (player) or who just acted (AI),
     *         or null if the battle is over.
     */
    public ICombatEntity processNextTurn() {
        if (isBattleOver()) {
            return null;
        }
        if (turnQueue.isEmpty()) {
            refillQueue();
        }

        ICombatEntity actor = turnQueue.pollFirst();
        if (actor == null || !actor.isAlive()) {
            // skip dead actors
            return processNextTurn();
        }

        if (actor.getTeam() == Team.PLAYER) {
            // pause: controller must call playerResolved() next
            return actor;
        }

        // AI's turn: execute immediately and re‐queue
        actor.takeTurn(this);
        turnQueue.addLast(actor);
        return actor;
    }

    /** Must be called once after the player’s action is executed. */
    public void playerResolved() {
        ICombatEntity player = getPlayerEntity();
        if (player.isAlive()) {
            turnQueue.addLast(player);
        }
    }

    /** @return true if either all players or all enemies are dead. */
    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);

        boolean anyEnemy = allEntities.stream()
                .filter(e -> e.getTeam() != Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);

        return !(anyPlayer && anyEnemy);
    }

    /** @return the single player combatant. */
    public ICombatEntity getPlayerEntity() {
        return allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player in battle"));
    }

    /** @return all alive players (usually just one). */
    public List<ICombatEntity> getPlayers() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .filter(e -> e.getTeam() == Team.PLAYER)
                .collect(Collectors.toList());
    }

    /** @return all alive enemies relative to the given team. */
    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .filter(e -> e.getTeam() != team)
                .collect(Collectors.toList());
    }
}
