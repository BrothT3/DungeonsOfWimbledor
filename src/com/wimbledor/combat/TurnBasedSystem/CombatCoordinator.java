package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Orchestrates round/turn progression, delegates to TurnProcessor,
 * and accumulates a RoundResult.
 */
public class CombatCoordinator {
    private final TurnProcessor processor;
    private final List<ICombatEntity> allEntities;
    private RoundTurnQueue    currentQueue;
    private RoundResult       currentRoundResult;
    private int               roundCounter = 1;

    public CombatCoordinator(ICombatEntity player, List<ICombatEntity> enemies) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(enemies);
        this.processor   = new TurnProcessor((com.wimbledor.entities.Player)player);
        this.allEntities = new java.util.ArrayList<>();
        allEntities.add(player);
        allEntities.addAll(enemies);
        startNewRound();
    }

    private void startNewRound() {
        var alive = allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(e -> -e.getDerived(com.wimbledor.combat.enums.DerivedStat.SPEED)))
                .toList();

        currentQueue       = new RoundTurnQueue(alive);
        currentRoundResult = new RoundResult(roundCounter++, alive);
    }

    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .anyMatch(e -> e.isAlive() && e.getTeam() == Team.PLAYER);
        boolean anyEnemy  = allEntities.stream()
                .anyMatch(e -> e.isAlive() && e.getTeam() != Team.PLAYER);
        return !(anyPlayer && anyEnemy);
    }

    public List<ICombatEntity> getAliveTurnOrder() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(e -> -e.getDerived(com.wimbledor.combat.enums.DerivedStat.SPEED)))
                .toList();
    }

    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(e -> e.isAlive() && e.getTeam() != team)
                .toList();
    }

    /**
     * Advance exactly one turn (AI or Player) and return its TurnResult.
     */
    public TurnResult nextTurn() {
        if (isBattleOver()) {
            return TurnResult.battleOver();
        }

        ICombatEntity actor = currentQueue.nextTurn();
        List<ICombatEntity> order = getAliveTurnOrder();
        List<ICombatEntity> foes  = getEnemiesOf(actor.getTeam());

        final TurnResult result;
        if (actor.getTeam() == Team.PLAYER) {
            // Pause so UI can show player buttons
            result = processor.getPlayerPause(order, foes);
        } else {
            // Let AI act immediately
            result = processor.handleAiTurn(actor, order, foes);
        }

        currentRoundResult.addTurnResult(result);
        return result;
    }

    /**
     * Called when the player clicks an action button.
     */
    public TurnResult playerAct(ICombatAction action, List<ICombatEntity> targets) {
        var order = getAliveTurnOrder();
        var foes  = getEnemiesOf(Team.PLAYER);

        TurnResult result = processor.handlePlayerTurn(order, foes, action, targets);
        currentRoundResult.addTurnResult(result);
        return result;
    }

    /**
     * Look ahead at the next AI action so CombatLoop can schedule timing.
     */
    public ICombatAction peekNextAiAction() {
        ICombatEntity next = currentQueue.peekNext();
        if (next == com.wimbledor.engine.GameContext.getPlayer()) return null;
        Decision d = next.decideNextAction(getEnemiesOf(next.getTeam()));
        return d != null ? d.action : null;
    }
}
