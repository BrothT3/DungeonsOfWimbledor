// src/com/wimbledor/engine/GameContext.java
package com.wimbledor.engine;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.combat.TurnManager;
import com.wimbledor.entities.Player;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * A simple global context for wiring together narrative encounters,
 * battles, and progression.
 *
 * You must call:
 *   GameContext.setPlayer(yourPlayer);
 * And:
 *   GameContext.setLogger(yourLogConsumer);
 */
public class GameContext {
    private static Player player;
    private static Consumer<String> LOG;

    /** Set a UI‐safe logger callback (e.g. logPanel::append). */
    public static void setLogger(Consumer<String> log) {
        LOG = log;
    }

    /** Thread‐safe logging via SwingUtilities.invokeLater. */
    public static void log(String msg) {
        if (LOG != null) SwingUtilities.invokeLater(() -> LOG.accept(msg));
    }

    /** Store your singleton Player. */
    public static void setPlayer(Player p) {
        player = p;
    }

    /** Retrieve the singleton Player. */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Immediately launches a battle using the given BattleCard.
     * Post‐combat flow is now handled entirely in CardController.resolve(...).
     */
    public static TurnManager startBattleWith(Player p, BattleCard card) {
        List<com.wimbledor.entities.ICombatEntity> combatants = card.getMonsters();
        // No more narrative callback here—CardController will drive post‐combat.
        return new TurnManager(player, combatants);
    }

    /**
     * (Optional) If you still need to inject
     * a standalone narrative card mid‐run,
     * call this from your controller directly.
     */
    public static void presentEncounter(ICard encounterCard) {
        // You can have your CardController handle this,
        // e.g. controller.showSpecific(encounterCard);
        throw new UnsupportedOperationException(
                "presentEncounter() is now deprecated; use your controller directly."
        );
    }
}
