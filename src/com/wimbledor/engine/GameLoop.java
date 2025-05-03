package com.wimbledor.engine;

import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.Player;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.wimbledor.entities.ICombatEntity;

/**
 * Logic-side timer that drives TurnManager independently of UI.
 * Schedules fixed-rate ticks that advance the battle logic.
 */
public class GameLoop {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private TurnManager turnManager;
    private long tickRateMs;

    /**
     * Start the loop for a battle. Non-blocking; logic runs on scheduler thread.
     //* @param player the player entity
    // * @param enemies list of enemies
    // * @param onBattleOver callback when battle ends
     * @param tickRateMs how often to call update
     */
    public void startBattle(TurnManager tm, long tickRateMs) {
        this.turnManager = tm;
        scheduler.scheduleAtFixedRate(
                () -> turnManager.update(tickRateMs), tickRateMs, tickRateMs, TimeUnit.MILLISECONDS
        );
    }

    /**
     * Inform the loop that player action is complete.
     */
    public void playerResolved() {
        if (turnManager != null) turnManager.playerActionResolved();
    }

    /**
     * Stop the scheduler gracefully.
     */
    public void stop() {
        scheduler.shutdownNow();
    }
}
