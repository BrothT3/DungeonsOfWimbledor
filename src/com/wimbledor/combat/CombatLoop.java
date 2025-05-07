package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.combat.TurnBasedSystem.TurnResult;
import com.wimbledor.engine.DeltaTimer;

import javax.swing.SwingUtilities;

public class CombatLoop implements Runnable {
    private ICombatAction nextAction;

    public interface Listener {
        void onTurnResult(TurnResult result);
    }

    private final CombatCoordinator coordinator;
    private final Listener            listener;
    private final DeltaTimer          timer   = new DeltaTimer();
    private volatile boolean          running = false;

    public CombatLoop(CombatCoordinator coordinator, Listener listener) {
        this.coordinator = coordinator;
        this.listener    = listener;
    }

    /** Starts the background thread. */
    public void start() {
        nextAction = coordinator.peekNextAiAction();
        if (running) return;
        running = true;
        new Thread(this, "CombatLoop").start();
    }

    /** Stops after the current iteration. */
    public void stop() {
        running = false;
    }
    public void primeNextTurn() {
        this.nextAction = coordinator.peekNextAiAction();
        timer.reset();
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            // 1) Look at the next AI action. If null, that means either
            //    (a) next actor is the player, or (b) battle’s over.
            ICombatAction nextAi = nextAction;

            if (nextAi == null) {
                // *** Player’s turn (or BATTLE_OVER) happens immediately ***
                TurnResult result = coordinator.nextTurn();
                SwingUtilities.invokeLater(() -> listener.onTurnResult(result));

                // if it really was the player’s turn, we now pause here
                // until CombatController.onPlayerAction() calls start() again.
                if (result.getType() == TurnResult.Type.PLAYER_TURN) {
                    running = false;
                    return;
                }

                // if battle over, stop looping entirely
                if (result.getType() == TurnResult.Type.BATTLE_OVER) {
                    running = false;
                    return;
                }

                // otherwise (rare: maybe runToPause logic), loop continues
            }
            else {
                // *** AI turn: wait for its duration, then fire one turn ***
                timer.update();
                if (timer.reached(nextAi.getDurationSeconds())) {
                    TurnResult result = coordinator.nextTurn();
                    SwingUtilities.invokeLater(() -> listener.onTurnResult(result));
                    nextAction = coordinator.peekNextAiAction();
                    timer.reset();

                    if (result.getType() == TurnResult.Type.BATTLE_OVER) {
                        running = false;
                        return;
                    }
                }
            }

            // tiny sleep so we don’t busy-spin
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
