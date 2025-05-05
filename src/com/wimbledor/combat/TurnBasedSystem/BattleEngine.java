package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.CombatExecutor;

import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.combat.AttackResult;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnResult;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Team;
import com.wimbledor.engine.GameContext;

import java.util.List;

/**
 * Orchestrates the full combat loop: pulls actors by speed,
 * pauses on player turn, and processes AI turns via CombatExecutor.
 */
public class BattleEngine {
    private final TurnQueue turnQueue;

    public BattleEngine(TurnQueue turnQueue) {
        this.turnQueue = turnQueue;
    }

    /**
     * Execute exactly one actor’s turn:
     * - Returns AI_TURN with results when an AI acts
     * - Returns PLAYER_TURN with foes when it’s the player’s turn
     * - Returns BATTLE_OVER when no actors remain
     */
    public TurnResult step() {
        // 1) capture the full queue for UI
        List<ICombatEntity> actors = turnQueue.getTurnOrder();

        // 2) get the next actor
        ICombatEntity actor = turnQueue.processNextTurn();
        if (actor == null) {
            return TurnResult.battleOver();
        }

        // 3) if it's the player, pause and return full state
        if (actor.getTeam() == Team.PLAYER) {
            List<ICombatEntity> foes    = turnQueue.getEnemiesOf(Team.PLAYER);
            List<ICombatAction> actions = GameContext.getPlayer().getAvailableActions();
            return TurnResult.playerTurn(actors, foes, actions, List.of());
        }

        // 4) otherwise it's an AI turn—execute it
        Decision decision = actor.decideNextAction(turnQueue.getEnemiesOf(actor.getTeam()));
        List<AttackResult> results = CombatExecutor.execute(
                decision.action, actor, decision.targets
        );
        // update foes after the AI action
        List<ICombatEntity> foesAfter = turnQueue.getEnemiesOf(Team.PLAYER);
        return TurnResult.aiTurn(actors, foesAfter, results);
    }


    /**
     * Runs step() repeatedly until reaching PLAYER_TURN or BATTLE_OVER.
     */
    public TurnResult runToPause() {
        TurnResult result;
        do {
            result = step();
        } while (result.getType() == TurnResult.Type.AI_TURN);
        return result;
    }

    /**
     * Called when the player selects an action + targets:
     * executes it, re-queues the player, then runs AI to next pause.
     */
    public TurnResult playerAct(
            ICombatAction action,
            List<ICombatEntity> targets
    ) {
        // Execute player action
        List<AttackResult> playerResults = CombatExecutor.execute(
                action,
                GameContext.getPlayer(),
                targets
        );

        // Requeue player by speed order
        turnQueue.playerResolved();

        // Hand off to AI until next player turn or battle end
        return runToPause();
    }
}