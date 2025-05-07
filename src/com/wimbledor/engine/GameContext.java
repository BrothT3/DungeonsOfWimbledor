// src/com/wimbledor/engine/GameContext.java
package com.wimbledor.engine;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.effects.StatusEffectManager;
import com.wimbledor.entities.Player;

import javax.swing.SwingUtilities;
import java.util.function.Consumer;

public class GameContext {
    private static Player player;
    private static Consumer<String> LOG;

    // <-- NEW: hold onto the current CombatCoordinator
    private static CombatCoordinator combatCoordinator;

    /**
     * Set a UI‐safe logger callback (e.g. logPanel::append).
     */
    public static void setLogger(Consumer<String> log) {
        LOG = log;
    }

    /**
     * Thread‐safe logging via SwingUtilities.invokeLater.
     */
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

    /** Fetch the currently active CombatCoordinator (if any). */
    public static CombatCoordinator getCombatCoordinator() {
        return combatCoordinator;
    }

    /**
     * Immediately launches a battle using the given BattleCard,
     * spins up a CombatCoordinator for it, caches it, and returns it.
     */
    public static CombatCoordinator startBattleWith(Player p, BattleCard card) {
        setPlayer(p);

        CombatCoordinator coord = new CombatCoordinator(
                p,
                card.getMonsters()
        );
        combatCoordinator = coord;
        return coord;
    }

    // rest of your existing methods…
    private static StatusEffectManager effects;
    public static void setStatusEffectManager(StatusEffectManager m) { effects = m; }
    public static StatusEffectManager getStatusEffectManager()    { return effects; }
}
