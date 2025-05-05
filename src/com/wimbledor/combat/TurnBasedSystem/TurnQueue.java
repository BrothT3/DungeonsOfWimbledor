// src/com/wimbledor/combat/TurnBasedSystem/TurnQueue.java
package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;
import com.wimbledor.combat.enums.DerivedStat;
import java.util.*;

/**
 * Pure speed-sorted queue of combatants.
 * Only responsible for managing turn order by speed.
 */
public class TurnQueue {
    private final List<ICombatEntity> allEntities = new ArrayList<>();
    private final Deque<ICombatEntity> queue = new ArrayDeque<>();

    public TurnQueue(ICombatEntity player, List<ICombatEntity> enemies) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(enemies, "enemies");
        allEntities.add(player);
        allEntities.addAll(enemies);
        refillQueue();
    }

    /**
     * Populate the queue with alive combatants, sorted by descending speed.
     */
    private void refillQueue() {
        queue.clear();
        allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(e -> -e.getDerived(DerivedStat.SPEED)))
                .forEach(queue::addLast);
    }

    /**
     * Retrieve the next actor, or null if combat has ended.
     */
    public ICombatEntity processNextTurn() {
        if (isBattleOver()) return null;
        if (queue.isEmpty()) refillQueue();

        ICombatEntity actor = queue.pollFirst();
        if (actor == null || !actor.isAlive()) {
            return processNextTurn(); // skip dead
        }
        return actor;
    }

    public List<ICombatEntity> getTurnOrder() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(e -> -e.getDerived(DerivedStat.SPEED)))
                .toList();
    }

    /**
     * Re-queue the player after they act.
     */
    public void playerResolved() {
        for (ICombatEntity e : allEntities) {
            if (e.getTeam() == Team.PLAYER && e.isAlive()) {
                queue.addLast(e);
                break;
            }
        }
    }

    /**
     * Check if one side has been wiped out.
     */
    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .filter(e -> e.getTeam() == Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        boolean anyEnemy = allEntities.stream()
                .filter(e -> e.getTeam() != Team.PLAYER)
                .anyMatch(ICombatEntity::isAlive);
        return !(anyPlayer && anyEnemy);
    }
    public ICombatEntity peekNext() {
        if (queue.isEmpty()) refillQueue();
        return queue.peekFirst();
    }
    /**
     * List all living enemies of the given team.
     */
    public List<ICombatEntity> getEnemiesOf(Team team) {
        List<ICombatEntity> foes = new ArrayList<>();
        for (ICombatEntity e : allEntities) {
            if (e.isAlive() && e.getTeam() != team) {
                foes.add(e);
            }
        }
        return foes;
    }
}
