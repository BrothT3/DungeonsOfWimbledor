// src/com/wimbledor/ui/controller/GameController.java

package com.wimbledor.ui.controller;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.ICard;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.weapons.FierceAxe;
import com.wimbledor.ui.MainFrame;
import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.NarrativePanel;
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
        // 1) Set up global game context
        GameContext.setPlayer(player);
        EquipmentManager.getInstance().equipWeapon(new FierceAxe());

        // 2) Build UI and extract panels
        mainFrame = new MainFrame();
        CombatPanel combatPanel       = mainFrame.getCombatPanel();
        NarrativePanel narrativePanel = mainFrame.getNarrativePanel();
        PlayerInfoPanel playerInfo    = mainFrame.getPlayerInfoPanel();
        playerInfo.setPlayer(player);

        // 3) Set global logger
        GameContext.setLogger(combatPanel::appendLog);

        // 4) Load encounter deck
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());

        // 5) Initialize narrative controller
        narrativeController = new NarrativeController(
                deck,
                narrativePanel,
                player,
                playerInfo,
                () -> JOptionPane.showMessageDialog(
                        mainFrame,
                        "You’ve cleared the dungeon!",
                        "Victory",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // 6) Wire up combat entry from narrative
        narrativeController.setOnBattleStart((BattleCard battleCard, ICard aftermathCard) -> {
            mainFrame.showCombat();

            // a) Start & cache the coordinator in GameContext
            CombatCoordinator coord =
                    GameContext.startBattleWith(player, battleCard);

            // b) Create and start the CombatController
            CombatController combatController = new CombatController(
                    coord,
                    combatPanel,
                    playerInfo,
                    () -> {
                        mainFrame.showNarrative();
                        narrativeController.resumeAfterBattle();
                    }
            );

            combatController.setPendingAfterStage(aftermathCard);
            combatController.start();
        });

        // 7) Begin in narrative view
        mainFrame.showNarrative();
        narrativeController.start();
    }

    /** Launch helper. */
    public static void launch(Player player) {
        SwingUtilities.invokeLater(() -> new GameController(player));
    }
}
