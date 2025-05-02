package com.wimbledor.assets.encounterProviders;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.assets.encounters.StageOption;
import com.wimbledor.assets.monsters.GoblinCard;
import com.wimbledor.engine.EncounterProvider;

import java.util.List;

public class GoblinAmbushEncounter implements EncounterProvider {
    @Override
    public EncounterCard create() {
        EncounterStage sneakEnd = new EncounterStage(
                "You slip past the goblins unseen, heart pounding.",
                List.of(new StageOption("C", "Continue", p -> p.addExperience(5), null, null))
        );
        // Root
        EncounterStage root = new EncounterStage(
                "A pack of goblins emerges from the shadows, weapons raised!",
                List.of(
                        new StageOption("F", "Fight the goblins", p -> {
                        }, null,
                                new BattleCard("Sneaky Bastards!", List.of(new GoblinCard()))
                        ),
                        new StageOption("S", "Sneak past quietly", p -> {
                        }, sneakEnd, null)
                )
        );
        return new EncounterCard("Goblin Ambush", root);
    }
}
