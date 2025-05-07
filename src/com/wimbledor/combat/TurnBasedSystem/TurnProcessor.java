package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatExecutor;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import java.util.List;
import java.util.Objects;

/**
 * Responsible for executing a single turn: AI or player.
 */
public class TurnProcessor {
    private final ICombatEntity player;

    public TurnProcessor(ICombatEntity player) {
        this.player = Objects.requireNonNull(player);
    }

    /**
     * Executes a full AI turn.
     */
    public TurnResult handleAiTurn(
            ICombatEntity aiActor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemyTargets
    ) {
        Decision decision = aiActor.decideNextAction(enemyTargets);

        if (decision == null || decision.action == null || decision.targets.isEmpty()) {
            return TurnResult.aiTurn(aiActor, turnOrder, enemyTargets, List.of());
        }

        List<AttackResult> results = CombatExecutor.execute(
                decision.action, aiActor, decision.targets
        );

        return TurnResult.aiTurn(aiActor, turnOrder, enemyTargets, results);
    }

    /**
     * Executes the player's chosen action.
     */
    public TurnResult handlePlayerTurn(
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemyTargets,
            ICombatAction action,
            List<ICombatEntity> targets
    ) {
        List<AttackResult> results = CombatExecutor.execute(
                action, player, targets
        );

        return TurnResult.playerTurn(
                player, turnOrder, enemyTargets, player.getAvailableActions(), results
        );
    }

    /**
     * Builds the TurnResult when it's the player's turn to act (UI pause).
     */
    public TurnResult getPlayerPause(
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemyTargets
    ) {
        return TurnResult.playerTurn(
                player, turnOrder, enemyTargets, player.getAvailableActions(), null
        );
    }
}
