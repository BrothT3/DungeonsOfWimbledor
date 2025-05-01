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
        this.allEntities.addAll(combatants);
        this.turnQueue.addAll(combatants);
    }

    public void startNextTurn() {
        if (turnQueue.isEmpty()) refillQueue();
        currentEntity = turnQueue.poll();
    }

    public ICombatEntity getCurrentEntity() {
        return currentEntity;
    }

    private void refillQueue() {
        for (ICombatEntity e : allEntities) {
            if (e.getHP() > 0) turnQueue.add(e);
        }
    }

    public boolean isBattleOver() {
        Set<Team> aliveTeams = allEntities.stream()
                .filter(e -> e.getHP() > 0)
                .map(ICombatEntity::getTeam)
                .collect(Collectors.toSet());
        return aliveTeams.size() <= 1;
    }

    public List<ICombatEntity> getEntitiesOnTeam(Team team) {
        return allEntities.stream()
                .filter(e -> e.getHP() > 0 && e.getTeam() == team)
                .collect(Collectors.toList());
    }

    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(e -> e.getHP() > 0 && e.getTeam() != team)
                .collect(Collectors.toList());
    }

    public List<ICombatEntity> getAllEntities() {
        return allEntities.stream()
                .filter(e -> e.getHP() > 0)
                .collect(Collectors.toList());
    }
}
