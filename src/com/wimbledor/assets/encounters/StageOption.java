// src/com/wimbledor/cards/encounters/StageOption.java
package com.wimbledor.assets.encounters;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.entities.Player;

import java.util.function.Consumer;

/**
 * One choice on a given EncounterStage.
 */
public class StageOption {
    private final String code;           // unique key, e.g. "D", "S", "C"
    private final String label;          // button text
    private final String effectDescription;
    private final Consumer<Player> effect; // applied immediately on selection
    private final EncounterStage nextStage;  // next narrative stage (nullable)
    private final BattleCard nextBattle;        // if non-null, launch combat with this NPC

    public StageOption(String code,
                       String label,
                       String effectDescription,
                       Consumer<Player> effect,
                       EncounterStage nextStage,
                       BattleCard nextBattle) {
        this.code = code;
        this.label = label;
        // only keep description if an effect callback is provided
        this.effectDescription = (effect != null ? effectDescription : null);
        // default to no-op if null
        this.effect = (effect != null ? effect : p -> {});
        this.nextStage = nextStage;
        this.nextBattle = nextBattle;
    }
    public String getEffectDescription() {return effectDescription; }
    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public void apply(Player p) {
        effect.accept(p);
    }

    public EncounterStage getNextStage() {
        return nextStage;
    }

    public BattleCard getNextBattle() {
        return nextBattle;
    }
}
