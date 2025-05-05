// src/com/wimbledor/ui/controller/NarrativeController.java
package com.wimbledor.ui.controller;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.assets.encounters.StageOption;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.entities.Player;
import com.wimbledor.ui.view.NarrativePanel;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Drives narrative only: pulls EncounterCard from deck,
 * displays stages in NarrativePanel, handles user choices,
 * and notifies when a battle needs to start or when narrative ends.
 */
public class NarrativeController {
    private final EncounterDeck deck;
    private final NarrativePanel panel;
    private final Player player;
    /**
     * Callback: when a battle should start, along with its aftermath stage
     */
    private BiConsumer<BattleCard, EncounterStage> onBattleStart;
    /**
     * Callback: when the narrative deck is exhausted
     */
    private final Runnable onNarrativeComplete;

    private EncounterCard current;

    public NarrativeController(
            EncounterDeck deck,
            NarrativePanel panel,
            Player player,
            Runnable onNarrativeComplete
    ) {
        this.deck = deck;
        this.panel = panel;
        this.player = player;
        this.onNarrativeComplete = onNarrativeComplete;

        panel.setOptionClickListener(this::onOptionSelected);
    }
    public void setOnBattleStart(BiConsumer<BattleCard, EncounterStage> cb) {
        this.onBattleStart = cb;
    }
    /** Kick off the first narrative card. */
    public void start() {
        drawNextCard();
    }

    /** Called by GameController to resume after a battle. */
    public void resumeAfterBattle() {
        if (current != null) {
            panel.showStage(
                    current.getTitle(),
                    current.getCurrentStage().getDescription(),
                    buildLabels(current.getCurrentStage())
            );
        } else {
            drawNextCard();
        }
    }

    /** Draws and shows the next EncounterCard from the deck. */
    private void drawNextCard() {
        var card = deck.draw();
        if (card == null) {
            onNarrativeComplete.run();
            return;
        }
        if (card instanceof EncounterCard ec) {
            this.current = ec;
            showStage(ec);
        } else {
            // skip non-narrative cards
            drawNextCard();
        }
    }

    /** Displays the given encounter's current stage. */
    private void showStage(EncounterCard ec) {
        EncounterStage stage = ec.getCurrentStage();
        panel.showStage(
                ec.getTitle(),
                stage.getDescription(),
                buildLabels(stage)
        );
    }

    private List<String> buildLabels(EncounterStage stage) {
        return stage.getOptions().stream()
                .map(so -> so.getCode() + ". " + so.getLabel())
                .collect(Collectors.toList());
    }

    /** Handles option clicks or key presses. */
    private void onOptionSelected(String code) {
        var stage = current.getCurrentStage();
        for (StageOption so : stage.getOptions()) {
            if (!so.getCode().equals(code)) continue;
            // 1) apply effect
            so.apply(player);
            // 2) battle or next stage
            BattleCard battle = so.getNextBattle();
            EncounterStage next = so.getNextStage();
            if (battle != null) {
                onBattleStart.accept(battle, next);
            } else if (next != null) {
                current = new EncounterCard(current.getTitle(), next);
                panel.showStage(
                        current.getTitle(),
                        next.getDescription(),
                        buildLabels(next)
                );
            } else {
                drawNextCard();
            }
            // 3) if reward or status change, show info
            if (so.getEffectDescription() != null) {
                panel.appendInfo(so.getEffectDescription());
            }
            return;
        }
    }
}
