package com.wimbledor.combat.TurnBasedSystem;

import com.wimbledor.entities.ICombatEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all TurnResults for a single combat round.
 */
public class RoundResult {
    private final int roundNumber;
    private final List<TurnResult> turns = new ArrayList<>();
    private final List<ICombatEntity> initialTurnOrder;

    public RoundResult(int roundNumber, List<ICombatEntity> initialTurnOrder) {
        this.roundNumber = roundNumber;
        this.initialTurnOrder = initialTurnOrder;
    }

    public void addTurnResult(TurnResult turnResult) {
        turns.add(turnResult);
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<ICombatEntity> getInitialTurnOrder() {
        return initialTurnOrder;
    }

    public List<TurnResult> getTurns() {
        return turns;
    }

    public boolean isEmpty() {
        return turns.isEmpty();
    }

    public TurnResult getLastTurn() {
        return turns.isEmpty() ? null : turns.get(turns.size() - 1);
    }
}
