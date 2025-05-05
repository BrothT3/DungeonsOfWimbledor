package com.wimbledor.assets;

import com.wimbledor.entities.Player;

import java.util.function.Consumer;

public class CardOption {
    private final String code;                  // unique identifier (e.g. "A", "B")
    private final String label;                 // button label (e.g. "Drink the water")
    private final Consumer<Player> effect;      // effect to apply on click
    private final ICard nextCard;               // next EncounterCard or BattleCard
    private final String effectDescription;     // optional UI text (cyan log, tooltip)

    public CardOption(String code,
                      String label,
                      Consumer<Player> effect,
                      ICard nextCard,
                      String effectDescription) {
        this.code = code;
        this.label = label;
        this.effect = effect;
        this.nextCard = nextCard;
        this.effectDescription = effectDescription;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public Consumer<Player> getEffect() {
        return effect;
    }

    public ICard getNextCard() {
        return nextCard;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public void applyEffect(Player player) {
        if (effect != null) effect.accept(player);
    }
}
