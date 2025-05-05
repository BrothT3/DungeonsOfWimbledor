package com.wimbledor.ui.controller;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.assets.ICard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.entities.Player;
import com.wimbledor.ui.view.NarrativePanel;
import com.wimbledor.ui.view.PlayerInfoPanel;

import javax.swing.*;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Drives narrative only: pulls EncounterCard from deck,
 * displays stages in NarrativePanel, handles user choices,
 * and notifies when a battle needs to start or when narrative ends.
 */
public class NarrativeController {
    private final EncounterDeck deck;
    private final NarrativePanel panel;
    private final Player player;
    private final PlayerInfoPanel playerInfoPanel;
    private BiConsumer<BattleCard, ICard> onBattleStart;
    private final Runnable onNarrativeComplete;

    private EncounterCard current;

    public NarrativeController(
            EncounterDeck deck,
            NarrativePanel panel,
            Player player,
            PlayerInfoPanel playerInfoPanel,
            Runnable onNarrativeComplete
    ) {
        this.deck = deck;
        this.panel = panel;
        this.player = player;
        this.playerInfoPanel = playerInfoPanel;
        this.onNarrativeComplete = onNarrativeComplete;

        panel.setOptionClickListener(this::onOptionSelected);
    }

    public void setOnBattleStart(BiConsumer<BattleCard, ICard> cb) {
        this.onBattleStart = cb;
    }

    public void start() {
        drawNextCard();
    }

    public void resumeAfterBattle() {
        if (current != null) {
            panel.showStage(
                    current.getTitle(),
                    current.getDescription(),
                    current.getOptions()
            );
        } else {
            drawNextCard();
        }
    }

    private void drawNextCard() {
        ICard card = deck.draw();
        if (card == null) {
            onNarrativeComplete.run();
            return;
        }
        if (card instanceof EncounterCard ec) {
            current = ec;
            showStage(ec);
        } else {
            drawNextCard(); // skip non-narrative
        }
    }

    private void showStage(EncounterCard ec) {
        panel.showStage(
                ec.getTitle(),
                ec.getDescription(),
                ec.getOptions()
        );
    }

    private void onOptionSelected(String code) {
        var options = current.getOptions();
        for (CardOption opt : options) {
            if (!opt.getCode().equals(code)) continue;

            // 1. Apply effect
            opt.getEffect().accept(player);

            // 2. Transition to next card
            ICard next = opt.getNextCard();
            if (next instanceof EncounterCard ec) {
                current = ec;
                showStage(ec);
            } else if (next instanceof BattleCard bc) {
                onBattleStart.accept(bc, null);
            } else {
                drawNextCard();
            }

            // 3. Append effect description AFTER stage change
            if (opt.getEffectDescription() != null) {
                panel.appendInfo(opt.getEffectDescription());
            }

            // 4. Refresh player info AFTER changes
            playerInfoPanel.setPlayer(player);
            return;
        }
    }
    }



