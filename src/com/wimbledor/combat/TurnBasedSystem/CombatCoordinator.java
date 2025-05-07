// src/com/wimbledor/combat/TurnBasedSystem/CombatCoordinator.java
package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class CombatCoordinator {
    private final TurnProcessor processor;
    private final List<ICombatEntity> allEntities;
    private       RoundTurnQueue      currentQueue;
    private       RoundResult         currentRoundResult;
    private       int                 roundCounter = 1;

    public CombatCoordinator(ICombatEntity player, List<ICombatEntity> enemies) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(enemies);

        this.processor   = new TurnProcessor(player);
        this.allEntities = new java.util.ArrayList<>();
        allEntities.add(player);
        allEntities.addAll(enemies);

        startNewRound();
    }

    public TurnResult nextTurn() {
        if (isBattleOver()) {
            return TurnResult.battleOver();
        }

        ICombatEntity actor = currentQueue.nextTurn();
        // if queue was exhausted, start a new round
        if (actor == null) {
            startNewRound();
            actor = currentQueue.nextTurn();
        }

        List<ICombatEntity> order  = getAliveTurnOrder();
        List<ICombatEntity> foes   = getEnemiesOf(actor.getTeam());
        TurnResult result;

        if (actor.getTeam() == Team.PLAYER) {
            // pause and give UI the player’s actions
            result = processor.getPlayerPause(order, foes);
        } else {
            // resolve exactly one AI action
            result = processor.handleAiTurn(actor, order, foes);
        }

        currentRoundResult.addTurnResult(result);
        return result;
    }

    /** Peek the next AI action so CombatLoop can schedule its timing. */
    public ICombatAction peekNextAiAction() {
        ICombatEntity next = currentQueue.peekNext();
        if (next == null || next.getTeam() == Team.PLAYER) return null;
        Decision d = next.decideNextAction(getEnemiesOf(next.getTeam()));
        return d == null ? null : d.action;
    }
    private void startNewRound() {
        var aliveSorted = allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(e -> -e.getDerived(com.wimbledor.combat.enums.DerivedStat.SPEED)))
                .toList();

        currentQueue       = new RoundTurnQueue(aliveSorted, roundCounter);
        currentRoundResult = new RoundResult(roundCounter++, aliveSorted);
    }

    public boolean isBattleOver() {
        boolean anyPlayer = allEntities.stream()
                .anyMatch(e -> e.isAlive() && e.getTeam() == Team.PLAYER);
        boolean anyEnemy  = allEntities.stream()
                .anyMatch(e -> e.isAlive() && e.getTeam() != Team.PLAYER);
        return !(anyPlayer && anyEnemy);
    }

    /** Return the very next TurnResult, never skipping anything. */
    public TurnResult step() {
        if (isBattleOver()) {
            return TurnResult.battleOver();
        }

        ICombatEntity actor = currentQueue.nextTurn();
        // If queue exhausted, that'll return null → start new round
        if (actor == null) {
            startNewRound();
            actor = currentQueue.nextTurn();
        }

        List<ICombatEntity> order  = getAliveTurnOrder();
        List<ICombatEntity> enemies= getEnemiesOf(actor.getTeam());
        TurnResult result;

        if (actor.getTeam() == Team.PLAYER) {
            result = processor.getPlayerPause(order, enemies);
        } else {
            result = processor.handleAiTurn(actor, order, enemies);
        }
        System.out.println("the result in TurnResult.step() is "+result.getType());
        currentRoundResult.addTurnResult(result);
        return result;
    }

    /** When UI finally clicks a player-action button: */
    public TurnResult playerAct(ICombatAction action, List<ICombatEntity> targets) {
        List<ICombatEntity> order   = getAliveTurnOrder();
        List<ICombatEntity> enemies = getEnemiesOf(Team.PLAYER);

        TurnResult result = processor.handlePlayerTurn(order, enemies, action, targets);
        currentRoundResult.addTurnResult(result);
        return result;
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


}
