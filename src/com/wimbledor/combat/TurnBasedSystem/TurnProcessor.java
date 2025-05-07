// src/com/wimbledor/combat/TurnBasedSystem/TurnProcessor.java
package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatExecutor;
import com.wimbledor.combat.aiBrains.Decision;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;
import java.util.Objects;

public class TurnProcessor {
    private final ICombatEntity player;

    public TurnProcessor(ICombatEntity player) {
        this.player = Objects.requireNonNull(player);
    }

    /** Called when it becomes the player's turn (UI pause). */
    public TurnResult getPlayerPause(
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies
    ) {
        return TurnResult.playerTurn(
                player, turnOrder, enemies,
                player.getAvailableActions()
        );
    }

    /** Called after the player clicks an action button. */
    public TurnResult handlePlayerTurn(
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemyTargets,
            ICombatAction action,
            List<ICombatEntity> targets
    ) {
        List<AttackResult> results = CombatExecutor.execute(action, player, targets);

        // BEFORE: using playerTurn with non-null results
        // return TurnResult.playerTurn(player, turnOrder, enemyTargets, player.getAvailableActions(), results);

        // AFTER: use the dedicated factory
        return TurnResult.playerActed(
                player,
                turnOrder,
                enemyTargets,
                player.getAvailableActions(),
                results
        );
    }

    /** Called to resolve exactly one AI actor's turn. */
    public TurnResult handleAiTurn(
            ICombatEntity aiActor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies
    ) {
        Decision decision = aiActor.decideNextAction(enemies);
        List<AttackResult> results = (decision == null
                || decision.action == null
                || decision.targets.isEmpty())
                ? List.of()
                : CombatExecutor.execute(decision.action, aiActor, decision.targets);

        return TurnResult.aiTurn(
                aiActor,
                turnOrder,
                enemies,
                results
        );
    }
}