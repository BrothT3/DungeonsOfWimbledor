// src/com/wimbledor/ui/controller/CombatController.java
package com.wimbledor.ui.controller;

import com.wimbledor.assets.ICard;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatLoop;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.combat.TurnBasedSystem.TurnResult;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.PlayerInfoPanel;

import javax.swing.*;
import java.util.List;

public class CombatController implements CombatLoop.Listener {
    private final CombatPanel       panel;
    private final PlayerInfoPanel   playerInfo;
    private final CombatCoordinator coordinator;
    private final CombatLoop        loop;
    private final Runnable          onBattleOver;
    private boolean                 playerTurn    = false;
    private ICard                   pendingAfter;

    public CombatController(CombatCoordinator coordinator,
                            CombatPanel panel,
                            PlayerInfoPanel playerInfo,
                            Runnable onBattleOver)
    {
        this.coordinator  = coordinator;
        this.panel        = panel;
        this.playerInfo   = playerInfo;
        this.loop         = new CombatLoop(coordinator, this);
        this.onBattleOver = onBattleOver;

        panel.setActionClickListener(this::onPlayerAction);
    }

    public void start() {
        pendingAfter = null;
        playerTurn   = false;
        panel.clearLog();                // flush old combat log
        loop.start();                    // kicks off the first pause & AI loop
        playerInfo.setPlayer(GameContext.getPlayer());
    }

    public void setPendingAfterStage(ICard stage) {
        this.pendingAfter = stage;
    }

    private void onPlayerAction(ICombatAction action) {
        if (!playerTurn) return;

        // 1) Gather targets
        List<ICombatEntity> targets = panel.getSelectedEnemies();

        // 2) Tell the coordinator to execute exactly one player turn
        TurnResult result = coordinator.playerAct(action, targets);

        // 3) Immediately render that result in the UI
        onTurnResult(result);

        // 4) Hide the buttons (we’re now waiting on the AI)
        playerTurn = false;

        // 5) Prime the loop so it will pick up the very next AI turn
        loop.primeNextTurn();

        // 6) Refresh the right‐hand player stats
        playerInfo.setPlayer(GameContext.getPlayer());
    }

    @Override
    public void onTurnResult(TurnResult result) {
        // 1) Clear any selection highlight
        panel.clearSelection();

        // 2) Always refresh the turn‐order bar
        panel.updateTurnOrder(result.getTurnOrder());

        // 3) Always refresh the enemies panel from the player's perspective
        List<ICombatEntity> uiEnemies =
                coordinator.getEnemiesOf(GameContext.getPlayer().getTeam());
        panel.updateEnemies(uiEnemies);

        // 4) Handle the three cases
        switch (result.getType()) {
            case PLAYER_TURN -> {
                // Show the player's buttons
                //System.out.println("Player Turn");
                playerTurn = true;
                panel.updateActions(result.getPlayerActions());
            }

            case AI_TURN -> {
                // Hide/disable player buttons
                playerTurn = false;
                //System.out.println("AI Turn");
                panel.updateActions(null);
            }

            case BATTLE_OVER -> {
                playerTurn = false;
                panel.updateActions(null);
                panel.appendLog("\n-- Battle Over --\n");
                loop.stop();
                onBattleOver.run();
            }
        }


        // 5) Refresh the player‐info panel
        playerInfo.setPlayer(GameContext.getPlayer());
    }
}
