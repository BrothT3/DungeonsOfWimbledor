// src/com/wimbledor/ui/controller/GameController.java
package com.wimbledor.ui.controller;

import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.combat.TurnBasedSystem.*;

import com.wimbledor.entities.Player;
import com.wimbledor.ui.MainFrame;
import com.wimbledor.ui.view.NarrativePanel;
import com.wimbledor.ui.view.CombatPanel;

import javax.swing.*;
import java.util.List;

/**
 * Top‐level orchestrator. Boots the UI, spawns narrative & combat controllers,
 * and swaps the left panes between narrative and combat mode.
 */
public class GameController {
    private final MainFrame          mainFrame;
    private final NarrativeController narrativeController;
    private final CombatController    combatController;

    public GameController(Player player) {
        mainFrame = new MainFrame();

        // 1) Instantiate NarrativeController without battle callback
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());
        NarrativePanel narrativePanel = mainFrame.getNarrativePanel();
        narrativeController = new NarrativeController(
                deck,
                narrativePanel,
                player,
                /* onNarrativeComplete */ () -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "You’ve cleared the dungeon!", "Victory",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        );

        // 2) Instantiate CombatController without narrative callback
        CombatPanel combatPanel = mainFrame.getCombatPanel();
        BattleEngine engine = new BattleEngine(new TurnQueue(player, List.of()));
        combatController = new CombatController(
                engine,
                combatPanel,
                /* onBattleOver */ () -> {
            mainFrame.showNarrative();
            narrativeController.resumeAfterBattle();
        }
        );

        // 3) NOW wire the Narr->Combat callback
        narrativeController.setOnBattleStart((battleCard, aftermath) -> {
            mainFrame.showCombat();
            combatController.setPendingAfterStage(aftermath);
            combatController.start();
        });

        // 4) Kick off
        mainFrame.showNarrative();
        narrativeController.start();
    }



    /** Helper to launch the whole game on the EDT. */
    public static void launch(Player player) {
        SwingUtilities.invokeLater(() -> new GameController(player));
    }
}
