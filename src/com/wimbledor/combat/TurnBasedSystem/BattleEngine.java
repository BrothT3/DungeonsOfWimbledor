// src/com/wimbledor/combat/TurnBasedSystem/BattleEngine.java
package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatExecutor;
import com.wimbledor.combat.TurnResult;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import java.util.List;

/**
 * Controls progression of turn-based combat.
 */
public class BattleEngine {
    private final Player player;
    private final List<ICombatEntity> enemies;
    private final TurnQueue queue;

    public BattleEngine(Player player, List<ICombatEntity> enemies) {
        this.player = player;
        this.enemies = enemies;
        this.queue = new TurnQueue(player, enemies);
    }

    public TurnResult runToPause() {
        if (queue.isBattleOver()) {
            return TurnResult.battleOver();
        }

        while (!queue.isBattleOver()) {
            ICombatEntity actor = queue.processNextTurn();
            if (actor == null || !actor.isAlive()) continue;

            if (actor == player) {
                return TurnResult.playerTurn(
                        queue.getTurnOrder(),
                        queue.getEnemiesOf(actor.getTeam()),
                        player.getAvailableActions(),
                        null
                );
            } else {
                return TurnResult.aiTurn(
                        queue.getTurnOrder(),
                        queue.getEnemiesOf(actor.getTeam()),
                        List.of()  // no results yet
                );
            }
        }

        return TurnResult.battleOver();
    }

    public TurnResult step() {
        if (queue.isBattleOver()) {
            return TurnResult.battleOver();
        }

        ICombatEntity actor = queue.processNextTurn();
        if (actor == null || !actor.isAlive()) {
            return TurnResult.battleOver();
        }

        if (actor == player) {
            return TurnResult.playerTurn(
                    queue.getTurnOrder(),
                    queue.getEnemiesOf(actor.getTeam()),
                    player.getAvailableActions(),
                    List.of()
            );
        } else {
            List<ICombatEntity> foes = queue.getEnemiesOf(actor.getTeam());
            Decision decision = actor.decideNextAction(foes);

            if (decision == null || decision.targets.isEmpty()) {
                return TurnResult.aiTurn(queue.getTurnOrder(), foes, List.of());
            }

            List<AttackResult> results = CombatExecutor.execute(decision.action, actor, decision.targets);
            return TurnResult.aiTurn(queue.getTurnOrder(), foes, results);
        }
    }
    public ICombatAction peekNextAiAction() {
        ICombatEntity next = queue.peekNext();
        if (next == null || next == GameContext.getPlayer()) return null;

        List<ICombatEntity> foes = queue.getEnemiesOf(next.getTeam());
        Decision decision = next.decideNextAction(foes);
        return (decision != null) ? decision.action : null;
    }
    public TurnResult playerAct(ICombatAction action, List<ICombatEntity> targets) {
        List<AttackResult> results = CombatExecutor.execute(action, player, targets);
        return TurnResult.playerTurn(
                queue.getTurnOrder(),
                queue.getEnemiesOf(player.getTeam()),
                player.getAvailableActions(),
                results
        );
    }

    public boolean isBattleOver() {
        return queue.isBattleOver();
    }
}
