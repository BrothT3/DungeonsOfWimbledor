package GameWorld;

import GameWorld.Enums.Team;
import GameWorld.Interfaces.ICombatEntity;

import java.util.*;
import java.util.stream.Collectors;

public class TurnManager {
    private final PriorityQueue<ICombatEntity> turnQueue =
            new PriorityQueue<>(Comparator.comparingInt(ICombatEntity::getSpeed).reversed());
    private final List<ICombatEntity> allEntities = new ArrayList<>();
    private ICombatEntity currentEntity;

    public TurnManager(List<ICombatEntity> combatants) {
        // 1) record everyone
        this.allEntities.addAll(combatants);
        // 2) seed the queue with only the ones still alive
        refillQueue();
        // 3) immediately pull off the first turn
        startNextTurn();
    }

    /** Advance to the next alive actor. */
    public void startNextTurn() {
        if (turnQueue.isEmpty()) refillQueue();
        currentEntity = turnQueue.poll();
    }

    /** Never returns null: will auto‐advance if you forgot. */
    public ICombatEntity getCurrentEntity() {
        if (currentEntity == null) {
            startNextTurn();
        }
        return currentEntity;
    }

    private void refillQueue() {
        for (ICombatEntity e : allEntities) {
            if (e.getHP() > 0) turnQueue.add(e);
        }
    }

    public boolean isBattleOver() {
        long aliveTeams = allEntities.stream()
                .filter(e -> e.getHP() > 0)
                .map(ICombatEntity::getTeam)
                .distinct()
                .count();
        return aliveTeams <= 1;
    }



    public List<ICombatEntity> getEntitiesOnTeam(Team team) {
        return allEntities.stream()
                .filter(e -> e.getHP() > 0 && e.getTeam() == team)
                .collect(Collectors.toList());
    }

    public List<ICombatEntity> getEnemiesOf(Team team) {
        List<ICombatEntity> var = allEntities.stream()
                .filter(e -> e.getHP() > 0 && e.getTeam() != team)
                .collect(Collectors.toList());
        System.out.println("Enemy list size = " + var.size());
        return var;
    }

    public List<ICombatEntity> getAllEntities() {
        return allEntities.stream()
                .filter(e -> e.getHP() > 0)
                .collect(Collectors.toList());
    }
}
