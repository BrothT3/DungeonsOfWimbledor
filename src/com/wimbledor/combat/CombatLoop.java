package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.combat.TurnBasedSystem.TurnResult;
import com.wimbledor.engine.DeltaTimer;

import javax.swing.SwingUtilities;

/**
 * Runs a delta-time loop on its own thread, stepping
 * the CombatCoordinator when the current action’s duration elapses.
 */
public class CombatLoop implements Runnable {
    public interface Listener {
        void onTurnResult(TurnResult result);
    }

    private final CombatCoordinator engine;
    private final Listener listener;
    private final DeltaTimer timer = new DeltaTimer();
    private volatile boolean running;
    private ICombatAction nextAction;

    public CombatLoop(CombatCoordinator engine, Listener listener) {
        this.engine = engine;
        this.listener = listener;
    }

    /** Kick off the first turn and schedule the next AI action. */
    public void start() {
        // 1) Fire the very first TurnResult
        TurnResult initial = engine.nextTurn();
        SwingUtilities.invokeLater(() -> listener.onTurnResult(initial));

        // 2) Schedule the next AI action (if any)
        nextAction = engine.peekNextAiAction();
        timer.reset();

        // 3) Start the loop thread
        running = true;
        new Thread(this, "CombatLoop").start();
    }

    /**
     * Called by the UI after it consumes a pause (player or AI),
     * so we can look ahead again for the next AI action.
     */
    public void primeNextTurn(TurnResult result) {
        nextAction = engine.peekNextAiAction();
        timer.reset();
    }

    /** Stops the loop after the current iteration. */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            timer.update();

            if (nextAction != null && timer.reached(nextAction.getDurationSeconds())) {
                // 1) Advance exactly one turn in the coordinator
                TurnResult result = engine.nextTurn();

                // 2) Dispatch to UI
                SwingUtilities.invokeLater(() -> listener.onTurnResult(result));

                // 3) If battle over, stop; otherwise schedule next AI action
                if (result.getType() == TurnResult.Type.BATTLE_OVER) {
                    running = false;
                    nextAction = null;
                } else {
                    nextAction = engine.peekNextAiAction();
                }

                timer.reset();
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
