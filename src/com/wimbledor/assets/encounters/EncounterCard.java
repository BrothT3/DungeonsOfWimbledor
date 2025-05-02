// src/com/wimbledor/cards/encounters/EncounterCard.java
package com.wimbledor.assets.encounters;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.assets.ICard;
import com.wimbledor.engine.GameContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps a tree of EncounterStages into an ICard for your MVC.
 * Selecting an option can:
 * • apply a Player effect,
 * • advance to another stage in THIS card, or
 * • immediately launch combat with a BaseNPC (via GameContext).
 */
public class EncounterCard implements ICard {
    private final String title;
    private EncounterStage current;

    public EncounterCard(String title, EncounterStage root) {
        this.title = title;
        this.current = root;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return current.getDescription();
    }

    @Override
    public List<CardOption> getOptions() {
        return current.getOptions().stream().map(so -> {
            return new CardOption(
                    so.getCode(),
                    so.getLabel(),
                    player -> {
                        // 1) apply narrative effect
                        so.apply(player);

                        // 2) if there's a battle card, launch it
                        BattleCard battle = so.getNextBattle();
                        if (battle != null) {
                            GameContext.startBattleWith(player, battle);
                            return;
                        }

                        // 3) else if there's another narrative stage, advance
                        if (so.getNextStage() != null) {
                            this.current = so.getNextStage();
                            return;
                        }

                        // 4) otherwise, encounter is done
                        GameContext.onEncounterComplete();
                    },
                    // if nextStage != null, clicking this option keeps you in the same EncounterCard
                    so.getNextStage() != null ? this : null
            );
        }).collect(Collectors.toList());
    }

    @Override
    public ICard onOptionSelected(String code) {
        for (StageOption so : current.getOptions()) {
            if (!so.getCode().equals(code)) continue;
            so.apply(GameContext.getPlayer());
            if (so.getNextBattle() != null) {
                return so.getNextBattle();
            }
            if (so.getNextStage() != null) {
                return new EncounterCard(title, so.getNextStage());
            }
            return null;
        }
        return null;
    }
}

