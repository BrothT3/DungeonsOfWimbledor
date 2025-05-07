package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatExecutor;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CombatCoordinator {
    private final TurnProcessor      processor;       // from :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
    private final List<ICombatEntity> allEntities;
    private       RoundTurnQueue      queue;
    private       int                 roundNumber = 1;

    public CombatCoordinator(ICombatEntity player, List<ICombatEntity> enemies) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(enemies);

        this.processor   = new TurnProcessor(player);
        this.allEntities = new java.util.ArrayList<>();
        allEntities.add(player);
        allEntities.addAll(enemies);

        this.queue = new RoundTurnQueue(sortedAlive(), roundNumber++);
    }

    /** Peek at the next AI action so the loop can schedule its delay. */
    public ICombatAction peekNextAiAction() {
        ICombatEntity next = queue.peekNext();
        if (next == null || next.getTeam() == Team.PLAYER) return null;
        Decision d = next.decideNextAction(getEnemiesOf(next.getTeam()));
        return (d == null ? null : d.action);
    }

    /**
     * Advance by exactly one turn.
     *  – rolls over to a new round if the queue exhausts
     *  – returns PLAYER_TURN to pause loop for input
     *  – or AI_TURN after immediately resolving one AI action
     */
    public TurnResult nextTurn() {
        // 1) get the next actor, rolling over as needed
        ICombatEntity actor = queue.nextTurn();
        if (actor == null) {
            // round exhausted → start new
            queue = new RoundTurnQueue(sortedAlive(), roundNumber++);
            actor = queue.nextTurn();
        }

        // 2) snapshot for UI
        List<ICombatEntity> order   = sortedAlive();
        List<ICombatEntity> foes    = getEnemiesOf(actor.getTeam());
        TurnResult          result;

        // 3) branch to TurnProcessor
        if (actor.getTeam() == Team.PLAYER) {
            result = processor.getPlayerPause(order, foes);
        } else {
            result = processor.handleAiTurn(actor, order, foes);
        }

        List<AttackResult> results = result.getResults();
        if (results != null) {
            for (AttackResult ar : results) {
                if (!ar.target().isAlive()) {
                    queue.removeDead(ar.target());
                }
            }
        }

        return result;
    }

    /**
     * Called from CombatController when the user clicks an action button.
     * Resolves the player’s action immediately, then purges any killed.
     */
    public TurnResult playerAct(ICombatAction action,
                                List<ICombatEntity> targets) {
        // 1) Let the TurnProcessor execute & log the attack(s)
        TurnResult result = processor.handlePlayerTurn(
                sortedAlive(),
                getEnemiesOf(Team.PLAYER),
                action,
                targets
        );

        // 2) Immediately remove any dead from the current queue
        for (AttackResult ar : result.getResults()) {
            if (!ar.target().isAlive()) {
                queue.removeDead(ar.target());
            }
        }

        return result;
    }

    /** For the “Turn Order” bar in the UI */
    public List<ICombatEntity> getTurnOrder() {
        return queue.upcoming();
    }

    /** For the enemies-panel in the UI */
    public List<ICombatEntity> getEnemiesOf(Team team) {
        return allEntities.stream()
                .filter(e -> e.isAlive() && e.getTeam() != team)
                .collect(Collectors.toList());
    }

    private List<ICombatEntity> sortedAlive() {
        return allEntities.stream()
                .filter(ICombatEntity::isAlive)
                .sorted(Comparator.comparingInt(
                        e -> -e.getDerived(com.wimbledor.combat.enums.DerivedStat.SPEED)
                ))
                .collect(Collectors.toList());
    }
}
