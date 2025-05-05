// src/com/wimbledor/ui/controller/CombatController.java
package com.wimbledor.ui.controller;

import com.wimbledor.combat.AttackResult;
import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatLoop;
import com.wimbledor.combat.TurnResult;
import com.wimbledor.combat.TurnBasedSystem.BattleEngine;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.ui.view.CombatPanel;

import java.util.List;

/**
 * Orchestrates in-combat flow: player picks actions, AI goes,
 * and when the battle ends we hand control back to the narrative.
 */
public class CombatController implements CombatLoop.Listener {
    private final CombatPanel panel;
    private final BattleEngine engine;
    private final CombatLoop loop;
    private final Runnable onBattleOver;

    /** If set, the narrative stage to show immediately after this fight. */
    private EncounterStage pendingAfterStage;

    public CombatController(BattleEngine engine,
                            CombatPanel panel,
                            Runnable onBattleOver) {
        this.engine        = engine;
        this.panel         = panel;
        this.loop          = new CombatLoop(engine, this);
        this.onBattleOver  = onBattleOver;

        panel.setActionClickListener(this::onPlayerAction);
    }

    /** Kicks off the loop when a battle begins. */
    public void start() {
        pendingAfterStage = null;
        panel.clearLog();
        loop.start();
    }

    /** Allows NarrativeController to prime the aftermath stage. */
    public void setPendingAfterStage(EncounterStage stage) {
        this.pendingAfterStage = stage;
    }

    /** Bound to UI combat-action buttons. */
    private void onPlayerAction(ICombatAction action) {
        List<ICombatEntity> targets = panel.getSelectedEnemies();
        // Execute immediately, then reflect it in our listener
        TurnResult result = engine.playerAct(action, targets);
        onTurnResult(result);
        // now the loop thread will continue with any AI turns
    }

    /** Called by CombatLoop on every PLAYER_TURN, AI_TURN, or BATTLE_OVER. */
    @Override
    public void onTurnResult(TurnResult result) {
        // clear any click highlight from last turn
        panel.clearSelection();

        switch (result.getType()) {
            case PLAYER_TURN -> {
                panel.updateTurnOrder(result.getActors());
                panel.updateEnemies(result.getEnemies());
                panel.updateActions(result.getPlayerActions());

                // log the player’s hits
                if (result.getPlayerResults() != null) {
                    for (AttackResult ar : result.getPlayerResults()) {
                        panel.appendLog(ar.action()
                                .getLogMessage(
                                        ar.actor(),
                                        ar.target(),
                                        ar.hit(),
                                        ar.crit(),
                                        ar.damage()
                                ) + "\n"
                        );
                    }
                }
            }

            case AI_TURN -> {
                panel.updateEnemies(result.getEnemies());
                panel.updateTurnOrder(result.getActors());

                // log each AI hit
                for (AttackResult ar : result.getAiResults()) {
                    panel.appendLog(ar.action()
                            .getLogMessage(
                                    ar.actor(),
                                    ar.target(),
                                    ar.hit(),
                                    ar.crit(),
                                    ar.damage()
                            ) + "\n"
                    );
                }
            }

            case BATTLE_OVER -> {
                panel.appendLog("\n-- Battle Over --\n");
                loop.stop();

                // hand back to narrative; any pendingAfterStage was set earlier
                onBattleOver.run();
            }
        }
    }
}
