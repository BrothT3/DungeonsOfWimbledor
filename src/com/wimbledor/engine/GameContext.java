package com.wimbledor.engine;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.Player;

import javax.swing.*;

/**
 * A simple global context for wiring together narrative encounters,
 * battles, and progression.
 * <p>
 * You must call:
 * GameContext.setPlayer(yourPlayer);
 * GameContext.setOnEncounterComplete(yourController::drawNext);
 */
public class GameContext {
    private static Player player;
    private static Runnable onEncounterComplete;
    private static GameLoop gameLoop;

    /**
     * Set the singleton Player early in your bootstrap (e.g. MainFrame).
     */
    public static void setPlayer(Player p) {
        player = p;
    }

    /**
     * Retrieve the singleton Player anywhere.
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Register the callback that should run whenever an encounter
     * finishes (either by narrative end or by battle end).
     * Typically your CardController.drawNext().
     */
    public static void setOnEncounterComplete(Runnable handler) {
        onEncounterComplete = handler;
    }

    /**
     * Call this when a narrative encounter tree finishes
     * or a battle ends; it advances to the next ICard.
     */
    public static void onEncounterComplete() {
        if (onEncounterComplete != null) {
            onEncounterComplete.run();
        }
    }

    /**
     * Immediately launches a battle using the given BattleCard.
     * When that battle finishes, onEncounterComplete() is called,
     * so your CardController will draw the next encounter card.
     */
    public static void startBattleWith(Player p, BattleCard card) {
        // Capture UI callback so it runs back on the EDT
        Runnable resumeNarrative = () -> SwingUtilities.invokeLater(onEncounterComplete);
        gameLoop = new GameLoop();
        gameLoop.startBattle(p,
                card.getMonsters(),
                resumeNarrative,
                TurnManager.getTurnDelayMs());
    }
    public static void playerActionResolved() {
        if (gameLoop != null) gameLoop.playerResolved();
    }

    /**
     * (Optional) If you ever need to force‐present a standalone encounter
     * card mid-run, you can call this. It simply delegates to the same
     * controller callback (by replacing the current card on screen).
     */
    public static void presentEncounter(ICard encounterCard) {
        // treat this as the end of the _previous_ one
        onEncounterComplete.run();
        // then manually inject this new one as the CURRENT card
        // in your controller you might need a dedicated method like:
        // cardController.showSpecific(encounterCard);
        // For now, we assume your controller is designed to accept this run.
    }
}
