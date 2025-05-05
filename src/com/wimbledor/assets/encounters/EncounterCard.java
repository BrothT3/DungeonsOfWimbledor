// src/com/wimbledor/assets/encounters/EncounterCard.java
package com.wimbledor.assets.encounters;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.assets.ICard;
import com.wimbledor.engine.GameContext;

import java.util.List;
import java.util.stream.Collectors;

public class EncounterCard implements ICard {
    private final String title;
    private EncounterStage current;

    public EncounterCard(String title, EncounterStage root) {
        this.title = title;
        this.current = root;
    }

    public EncounterStage getCurrentStage() {
        return current;
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
        return current.getOptions().stream()
                .map(so -> {
                    // 1) Build the effect consumer — applies only the effect
                    var effect = (java.util.function.Consumer<com.wimbledor.entities.Player>) player -> {
                        so.apply(player);
                    };

                    // 2) Build the next card to transition to
                    ICard nextCard;
                    if (so.getNextBattle() != null) {
                        nextCard = so.getNextBattle();
                    } else if (so.getNextStage() != null) {
                        nextCard = new EncounterCard(title, so.getNextStage());
                    } else {
                        nextCard = null;
                    }

                    // 3) Return full CardOption with effectDescription support
                    return new CardOption(
                            so.getCode(),
                            so.getLabel(),
                            effect,
                            nextCard,
                            so.getEffectDescription()
                    );
                })
                .collect(Collectors.toList());
    }

}
