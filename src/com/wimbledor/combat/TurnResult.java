// src/com/wimbledor/combat/TurnResult.java
package com.wimbledor.combat;

import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

/**
 * Represents the result of one turn in combat.
 */
public class TurnResult {
    public enum Type { PLAYER_TURN, AI_TURN, BATTLE_OVER }

    private final Type                    type;
    private final List<ICombatEntity>     actors;
    private final List<ICombatEntity>     enemies;
    private final List<ICombatAction>     playerActions;
    private final List<AttackResult>      aiResults;
    private final List<AttackResult>      playerResults;

    private TurnResult(
            Type type,
            List<ICombatEntity> actors,
            List<ICombatEntity> enemies,
            List<ICombatAction> playerActions,
            List<AttackResult> aiResults,
            List<AttackResult> playerResults
    ) {
        this.type          = type;
        this.actors        = actors;
        this.enemies       = enemies;
        this.playerActions = playerActions;
        this.aiResults     = aiResults;
        this.playerResults = playerResults;
    }

    /** Player’s turn: full state + their own AttackResults. */
    public static TurnResult playerTurn(
            List<ICombatEntity> actors,
            List<ICombatEntity> enemies,
            List<ICombatAction> playerActions,
            List<AttackResult>  playerResults
    ) {
        return new TurnResult(
                Type.PLAYER_TURN,
                actors,
                enemies,
                playerActions,
                /* aiResults= */       null,
                /* playerResults= */  playerResults
        );
    }

    /** AI just acted: full state + the AI’s AttackResults. */
    public static TurnResult aiTurn(
            List<ICombatEntity> actors,
            List<ICombatEntity> enemies,
            List<AttackResult>  aiResults
    ) {
        return new TurnResult(
                Type.AI_TURN,
                actors,
                enemies,
                /* playerActions= */  null,
                /* aiResults= */       aiResults,
                /* playerResults= */   null
        );
    }

    /** Combat is over; no further data. */
    public static TurnResult battleOver() {
        return new TurnResult(
                Type.BATTLE_OVER,
                /* actors= */          null,
                /* enemies= */         null,
                /* playerActions= */  null,
                /* aiResults= */       null,
                /* playerResults= */   null
        );
    }

    /** What kind of pause this is (player-turn, AI-turn, or battle-over). */
    public Type getType() {
        return type;
    }

    /** Full turn order for UI timeline. */
    public List<ICombatEntity> getActors() {
        return actors;
    }

    /** Current list of enemy targets. */
    public List<ICombatEntity> getEnemies() {
        return enemies;
    }

    /** Available actions for the player on their turn. */
    public List<ICombatAction> getPlayerActions() {
        return playerActions;
    }

    /** The results of the AI’s action(s). */
    public List<AttackResult> getAiResults() {
        return aiResults;
    }

    /** The results of the player’s own action(s). */
    public List<AttackResult> getPlayerResults() {
        return playerResults;
    }
}
