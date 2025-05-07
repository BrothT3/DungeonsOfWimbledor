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
 * Top-level: boots the game, swaps between narrative & combat.
 */
public class GameController {
    private final MainFrame          mainFrame;
    private final NarrativeController narrativeController;

    public GameController(Player player) {
        // 1) Global context
        GameContext.setPlayer(player);
        EquipmentManager.getInstance().equipWeapon(new FierceAxe());

        // 2) Build UI
        mainFrame      = new MainFrame();
        CombatPanel combatPanel       = mainFrame.getCombatPanel();
        NarrativePanel narrativePanel = mainFrame.getNarrativePanel();
        PlayerInfoPanel playerInfo    = mainFrame.getPlayerInfoPanel();
        playerInfo.setPlayer(player);

        // 3) Global logger to combat log
        GameContext.setLogger(combatPanel::appendLog);

        // 4) Load narrative deck
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());

        // 5) Narrative controller
        narrativeController = new NarrativeController(
                deck,
                narrativePanel,
                player,
                playerInfo,
                () -> JOptionPane.showMessageDialog(
                        mainFrame, "You’ve cleared the dungeon!", "Victory",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // 6) When narrative says “start battle”…
        narrativeController.setOnBattleStart((BattleCard battleCard, ICard aftermathCard) -> {
            mainFrame.showCombat();

            // 6a) new model
            Player p = GameContext.getPlayer();
            CombatCoordinator coord = GameContext.startBattleWith(p, battleCard);

            // 6b) wire up controller
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

        // 7) show narrative
        mainFrame.showNarrative();
        narrativeController.start();
    }

    public static void launch(Player player) {
        SwingUtilities.invokeLater(() -> new GameController(player));
    }
}
