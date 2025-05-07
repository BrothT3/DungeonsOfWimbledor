package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.combat.TurnBasedSystem.TurnResult;
import com.wimbledor.engine.DeltaTimer;

import javax.swing.SwingUtilities;

public class CombatLoop implements Runnable {
    public interface Listener {
        void onTurnResult(TurnResult result);
    }

    private final CombatCoordinator coord;
    private final Listener listener;
    private final DeltaTimer timer = new DeltaTimer();

    private volatile boolean running = false;
    private volatile boolean waitingForPlayer = false;

    public CombatLoop(CombatCoordinator coord, Listener listener) {
        this.coord = coord;
        this.listener = listener;
    }

    /**
     * Start the loop. Immediately fires exactly one turn (AI or player),
     * then continues in background to step through all AI turns until we hit a player turn,
     * at which point it will pause again until you call primeNextTurn().
     */
    public void start() {
        if (running) return;
        running = true;
        waitingForPlayer = false;

        // Fire the very first turn.
        TurnResult first = coord.nextTurn();
        SwingUtilities.invokeLater(() -> listener.onTurnResult(first));

        // If that was a player pause, go into waiting mode,
        // otherwise reset timer for the next AI.
        if (first.getType() == TurnResult.Type.PLAYER_TURN) {
            waitingForPlayer = true;
        } else if (first.getType() == TurnResult.Type.AI_TURN) {
            timer.reset();
        } else { // BATTLE_OVER
            running = false;
            return;
        }

        new Thread(this, "CombatLoop").start();
    }

    /**
     * Called by CombatController.onPlayerAction()
     * once the user has clicked their button. This
     * un-pauses the loop so it can fire the next AI turn.
     */
    public void primeNextTurn() {
        waitingForPlayer = false;
        timer.reset();
    }

    /**
     * Stops the loop after the current iteration.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            if (waitingForPlayer) {
                // parked on a PLAYER_TURN until primeNextTurn() is called
                sleep5ms();
                continue;
            }

            // peek at the next AI action
            ICombatAction nextAi = coord.peekNextAiAction();
            if (nextAi == null) {
                TurnResult r = coord.nextTurn();
                SwingUtilities.invokeLater(() -> listener.onTurnResult(r));

                switch (r.getType()) {
                    case BATTLE_OVER:
                        running = false;
                        break;
                    case PLAYER_TURN:
                        // really a player pause
                        waitingForPlayer = true;
                        break;
                    case AI_TURN:
                        // it's an AI of the new round—reset the timer and keep going
                        timer.reset();
                        break;
                }
            } else {
                // schedule the AI turn by its duration
                timer.update();
                if (timer.reached(nextAi.getDurationSeconds())) {
                    TurnResult aiResult = coord.nextTurn();
                    SwingUtilities.invokeLater(() -> listener.onTurnResult(aiResult));

                    if (aiResult.getType() == TurnResult.Type.BATTLE_OVER) {
                        running = false;
                    } else {
                        // reset for the following AI
                        timer.reset();
                    }
                }
            }

            sleep5ms();
        }
    }

    private void sleep5ms() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
