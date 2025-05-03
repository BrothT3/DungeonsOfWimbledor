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
                    // 1) Build the effect callback exactly as before
                    var effect = (java.util.function.Consumer<com.wimbledor.entities.Player>) player -> {
                        so.apply(player);
                        BattleCard battle = so.getNextBattle();
                        if (battle != null) {
                            GameContext.startBattleWith(player, battle);
                            return;
                        }
                        if (so.getNextStage() != null) {
                            this.current = so.getNextStage();
                            return;
                        }
                    };

                    // 2) HERE’S THE KEY CHANGE:
                    //    wrap the *next* stage in a brand-new EncounterCard,
                    //    even if there's a battle attached.
                    ICard nextCard = so.getNextStage() != null
                            ? new EncounterCard(title, so.getNextStage())
                            : null;

                    return new CardOption(
                            so.getCode(),
                            so.getLabel(),
                            effect,
                            nextCard        // now correctly points at the aftermath card
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public ICard onOptionSelected(String code) {
        for (StageOption so : current.getOptions()) {
            if (!so.getCode().equals(code)) continue;
            so.apply(GameContext.getPlayer());
            if (so.getNextBattle() != null)    return so.getNextBattle();
            if (so.getNextStage() != null)     return new EncounterCard(title, so.getNextStage());
            return null;
        }
        return null;
    }
}
