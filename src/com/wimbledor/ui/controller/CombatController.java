package com.wimbledor.ui.controller;

import com.wimbledor.assets.ICard;
import com.wimbledor.combat.*;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.TurnBasedSystem.BattleEngine;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.PlayerInfoPanel;
import com.wimbledor.engine.GameContext;

import java.util.List;

/**
 * Orchestrates in-combat flow: player picks actions, AI goes,
 * and when the battle ends we hand control back to the narrative.
 */
public class CombatController implements CombatLoop.Listener {
    private final CombatPanel panel;
    private final PlayerInfoPanel playerInfoPanel;
    private final BattleEngine engine;
    private final CombatLoop loop;
    private final Runnable onBattleOver;
    private boolean playerTurn = false;

    private ICard pendingAfterStage;

    public CombatController(BattleEngine engine,
                            CombatPanel panel,
                            PlayerInfoPanel playerInfoPanel,
                            Runnable onBattleOver) {
        this.engine = engine;
        this.panel = panel;
        this.playerInfoPanel = playerInfoPanel;
        this.loop = new CombatLoop(engine, this);
        this.onBattleOver = onBattleOver;

        panel.setActionClickListener(this::onPlayerAction);
    }

    public void start() {
        pendingAfterStage = null;
        panel.clearLog();
        playerTurn = false;
        loop.start();
        playerInfoPanel.setPlayer(GameContext.getPlayer());
    }

    public void setPendingAfterStage(ICard stage) {
        this.pendingAfterStage = stage;
    }

    private void onPlayerAction(ICombatAction action) {
        if (!playerTurn) return;

        List<ICombatEntity> targets = panel.getSelectedEnemies();
        TurnResult result = engine.playerAct(action, targets);
        loop.primeNextTurn(result);
        playerTurn = false; // Assume it's no longer player's turn until confirmed again


        onTurnResult(result);
        playerInfoPanel.setPlayer(GameContext.getPlayer());
    }

    @Override
    public void onTurnResult(TurnResult result) {
        panel.clearSelection();

        switch (result.getType()) {
            case PLAYER_TURN -> {
                playerTurn = true;

                panel.updateTurnOrder(result.getActors());
                panel.updateEnemies(result.getEnemies());
                panel.updateActions(result.getPlayerActions());

                if (result.getPlayerResults() != null) {
                    for (AttackResult ar : result.getPlayerResults()) {
                        panel.appendLog(ar.action().getLogMessage(
                                ar.actor(), ar.target(), ar.hit(), ar.crit(), ar.damage()
                        ) + "\n");
                    }
                }

                playerInfoPanel.setPlayer(GameContext.getPlayer());
            }

            case AI_TURN -> {
                playerTurn = false;
//                System.out.println("Rendering enemies:");
//                for (ICombatEntity e : result.getEnemies()) {
//                    System.out.println("- " + e.getName() + " (" + e.getTeam() + ")");
//                }
                System.out.println("=== UI ENEMY PANEL: getEnemies() ===");
                for (var e : result.getEnemies()) {
                    System.out.println(" - " + e.getName() + " (" + e.getTeam() + ")");
                }
                panel.updateEnemies(result.getEnemies());
                panel.updateTurnOrder(result.getActors());

                for (AttackResult ar : result.getAiResults()) {
                    panel.appendLog(ar.action().getLogMessage(
                            ar.actor(), ar.target(), ar.hit(), ar.crit(), ar.damage()
                    ) + "\n");
                }

                playerInfoPanel.setPlayer(GameContext.getPlayer());
            }

            case BATTLE_OVER -> {
                playerTurn = false;
                panel.appendLog("\n-- Battle Over --\n");
                loop.stop();
                onBattleOver.run();
            }
        }
    }
}
