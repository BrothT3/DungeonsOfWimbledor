package com.wimbledor.ui.controller;

import com.wimbledor.assets.ICard;
import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatLoop;
import com.wimbledor.combat.TurnBasedSystem.CombatCoordinator;
import com.wimbledor.combat.TurnBasedSystem.TurnResult;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.ui.view.CombatPanel;
import com.wimbledor.ui.view.PlayerInfoPanel;

import java.util.Collections;
import java.util.List;

/**
 * Binds the CombatLoop + Coordinator to the CombatPanel UI.
 */
public class CombatController implements CombatLoop.Listener {
    private final CombatPanel panel;
    private final PlayerInfoPanel playerInfoPanel;
    private final CombatCoordinator coordinator;
    private final CombatLoop loop;
    private final Runnable onBattleOver;
    private boolean playerTurn = false;
    private ICard pendingAfterStage;

    public CombatController(CombatCoordinator coordinator,
                            CombatPanel panel,
                            PlayerInfoPanel playerInfoPanel,
                            Runnable onBattleOver) {
        this.coordinator = coordinator;
        this.panel       = panel;
        this.playerInfoPanel = playerInfoPanel;
        this.loop        = new CombatLoop(coordinator, this);
        this.onBattleOver = onBattleOver;

        panel.setActionClickListener(this::onPlayerAction);
    }

    public void start() {
        pendingAfterStage = null;
        playerTurn = false;
        panel.clearLog();
        loop.start();
        playerInfoPanel.setPlayer(GameContext.getPlayer());
    }

    public void setPendingAfterStage(ICard stage) {
        this.pendingAfterStage = stage;
    }

    private void onPlayerAction(ICombatAction action) {
        if (!playerTurn) return;

        List<ICombatEntity> targets = panel.getSelectedEnemies();
        TurnResult result = coordinator.playerAct(action, targets);

        loop.primeNextTurn(result);
        playerTurn = false;

        onTurnResult(result);
        playerInfoPanel.setPlayer(GameContext.getPlayer());
    }

    @Override
    public void onTurnResult(TurnResult result) {
        panel.clearSelection();

        switch (result.getType()) {
            case PLAYER_TURN -> {
                playerTurn = true;
                panel.updateTurnOrder(result.getTurnOrder());
                panel.updateEnemies(
                        coordinator.getEnemiesOf(GameContext.getPlayer().getTeam())
                );
                panel.updateActions(result.getPlayerActions());
                playerInfoPanel.setPlayer(GameContext.getPlayer());
            }

            case AI_TURN -> {
                playerTurn = false;
                panel.updateTurnOrder(result.getTurnOrder());
                panel.updateEnemies(
                        coordinator.getEnemiesOf(GameContext.getPlayer().getTeam())
                );
                // hide player buttons until their next turn
                panel.updateActions(Collections.emptyList());

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
