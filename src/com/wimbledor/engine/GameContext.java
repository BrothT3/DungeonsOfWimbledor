// src/com/wimbledor/engine/GameContext.java
package com.wimbledor.engine;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.combat.TurnBasedSystem.TurnQueue;
import com.wimbledor.effects.StatusEffectManager;
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
    public static TurnQueue startBattleWith(Player p, BattleCard card) {
        List<com.wimbledor.entities.ICombatEntity> combatants = card.getMonsters();
        // No more narrative callback here—CardController will drive post‐combat.
        return new TurnQueue(player, combatants);
    }
    private static StatusEffectManager effects;

    public static void setStatusEffectManager(StatusEffectManager m) {
        effects = m;
    }
    public static StatusEffectManager getStatusEffectManager() {
        return effects;
    }
}
