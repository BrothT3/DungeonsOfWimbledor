// src/com/wimbledor/combat/TurnBasedSystem/TurnResult.java
package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

public class TurnResult {
    public enum Type { PLAYER_TURN, AI_TURN, BATTLE_OVER }

    private final Type                type;
    private final ICombatEntity       actor;
    private final List<ICombatEntity> turnOrder;
    private final List<ICombatEntity> enemies;
    private final List<ICombatAction> playerActions; // only on PLAYER_TURN
    private final List<AttackResult>  results;       // attack results

    private TurnResult(
            Type type,
            ICombatEntity actor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies,
            List<ICombatAction> playerActions,
            List<AttackResult> results
    ) {
        this.type          = type;
        this.actor         = actor;
        this.turnOrder     = turnOrder;
        this.enemies       = enemies;
        this.playerActions = playerActions;
        this.results       = results;
    }

    /** Pause here — it’s the player’s turn and we show buttons. */
    public static TurnResult playerTurn(
            ICombatEntity actor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies,
            List<ICombatAction> playerActions
    ) {
        return new TurnResult(
                Type.PLAYER_TURN,
                actor, turnOrder, enemies,
                playerActions,
                /* results */ null
        );
    }

    /** After the player clicks one of their buttons. */
    public static TurnResult playerActed(
            ICombatEntity actor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies,
            List<ICombatAction> playerActions,
            List<AttackResult> results
    ) {
        return new TurnResult(
                Type.PLAYER_TURN,
                actor, turnOrder, enemies,
                playerActions,
                results
        );
    }

    /** One AI actor just executed. */
    public static TurnResult aiTurn(
            ICombatEntity actor,
            List<ICombatEntity> turnOrder,
            List<ICombatEntity> enemies,
            List<AttackResult> results
    ) {
        return new TurnResult(
                Type.AI_TURN,
                actor, turnOrder, enemies,
                /* playerActions */ null,
                results
        );
    }

    /** Combat has ended. */
    public static TurnResult battleOver() {
        return new TurnResult(
                Type.BATTLE_OVER,
                /* actor */ null, null, null, null, null
        );
    }

    public Type getType() {
        return type;
    }

    public ICombatEntity getActor() {
        return actor;
    }

    public List<ICombatEntity> getTurnOrder() {
        return turnOrder;
    }

    public List<ICombatEntity> getEnemies() {
        return enemies;
    }

    public List<ICombatAction> getPlayerActions() {
        return playerActions;
    }

    public List<AttackResult> getResults() {
        return results;
    }
}
