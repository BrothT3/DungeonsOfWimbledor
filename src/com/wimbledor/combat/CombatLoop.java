// src/com/wimbledor/combat/CombatLoop.java
package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnBasedSystem.BattleEngine;
import com.wimbledor.engine.DeltaTimer;

import javax.swing.SwingUtilities;

/**
 * Runs a delta‐time loop on its own thread, stepping
 * the BattleEngine when the current action’s duration elapses.
 */
public class CombatLoop implements Runnable {
    public interface Listener {
        /** Called on every TurnResult (PLAYER_TURN, AI_TURN, BATTLE_OVER). */
        void onTurnResult(TurnResult result);
    }

    private final BattleEngine engine;
    private final Listener listener;
    private final DeltaTimer timer = new DeltaTimer();
    private volatile boolean running;
    private ICombatAction nextAction;

    public CombatLoop(BattleEngine engine, Listener listener) {
        this.engine = engine;
        this.listener = listener;
    }

    public void start() {
        // 1) Run to the first pause (player turn or AI turn)
        TurnResult initial = engine.runToPause();
        SwingUtilities.invokeLater(() -> listener.onTurnResult(initial));

        // 2) Prime next action ONLY if it’s the AI’s turn
        nextAction = initial.getType() == TurnResult.Type.AI_TURN
                ? engine.peekNextAiAction()
                : null;

        timer.reset();

        // 3) Kick off the loop thread
        running = true;
        new Thread(this, "CombatLoop").start();
    }

    public void primeNextTurn(TurnResult result) {
        nextAction = result.getType() == TurnResult.Type.AI_TURN
                ? engine.peekNextAiAction()
                : null;
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
                TurnResult result = engine.step();
                SwingUtilities.invokeLater(() -> listener.onTurnResult(result));

                if (result.getType() == TurnResult.Type.AI_TURN) {
                    nextAction = result.getType() == TurnResult.Type.AI_TURN
                            ? engine.peekNextAiAction()
                            : null;
                } else {
                    nextAction = null;
                    if (result.getType() == TurnResult.Type.BATTLE_OVER) {
                        running = false;
                    }
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
