package com.wimbledor.ui.controller;

import com.wimbledor.assets.ICard;
import com.wimbledor.combat.TurnBasedSystem.BattleEngine;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.engine.GameContext;
import com.wimbledor.assets.BattleCard;
import com.wimbledor.combat.TurnBasedSystem.TurnQueue;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.weapons.FierceAxe;
import com.wimbledor.ui.MainFrame;
import com.wimbledor.ui.view.NarrativePanel;
import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.PlayerInfoPanel;

import javax.swing.*;
import java.util.List;

/**
 * Top-level orchestrator. Boots the UI, spawns narrative & combat controllers,
 * and swaps the left panes between narrative and combat mode.
 */
public class GameController {
    private final MainFrame mainFrame;
    private final NarrativeController narrativeController;

    public GameController(Player player) {
        // 1) Set up shared game context
        GameContext.setPlayer(player);
        EquipmentManager.getInstance().equipWeapon(new FierceAxe()); // example weapon

        // 2) Build the UI and get panel references
        mainFrame = new MainFrame();
        CombatPanel combatPanel = mainFrame.getCombatPanel();
        NarrativePanel narrativePanel = mainFrame.getNarrativePanel();
        PlayerInfoPanel playerInfoPanel = mainFrame.getPlayerInfoPanel();
        playerInfoPanel.setPlayer(player);
        // 3) Optional: hook up log panel for global logging
        GameContext.setLogger(msg -> combatPanel.appendLog(msg));

        // 4) Load encounter deck
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());

        // 5) Initialize narrative controller
        narrativeController = new NarrativeController(
                deck,
                narrativePanel,
                player,
                playerInfoPanel, // 🆕
                () -> JOptionPane.showMessageDialog(
                        mainFrame,
                        "You’ve cleared the dungeon!",
                        "Victory",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // 6) Wire up battle start from narrative
        narrativeController.setOnBattleStart((BattleCard battleCard, ICard aftermathCard) -> {
            mainFrame.showCombat();

            // Create new turn queue and engine for this battle
            TurnQueue queue = GameContext.startBattleWith(player, battleCard);
            List<ICombatEntity> enemies = battleCard.getMonsters(); // Assuming battleCard holds them
            BattleEngine engine = new BattleEngine(player, enemies);

            // Create a fresh combat controller per battle
            CombatController combatController = new CombatController(
                    engine,
                    combatPanel,
                    playerInfoPanel,
                    /* onBattleOver */ () -> {
                mainFrame.showNarrative();
                narrativeController.resumeAfterBattle();
            }
            );

            // Register the aftermath card (wrapped EncounterCard)
            combatController.setPendingAfterStage(aftermathCard);
            combatController.start();
        });

        // 7) Start the game
        mainFrame.showNarrative();
        narrativeController.start();
    }

    /** Launch helper for external use (like from main method). */
    public static void launch(Player player) {
        SwingUtilities.invokeLater(() -> new GameController(player));
    }
}
