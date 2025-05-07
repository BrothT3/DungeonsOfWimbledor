package com.wimbledor.assets.encounterProviders;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.assets.encounters.StageOption;
import com.wimbledor.entities.monsters.GoblinCard;
import com.wimbledor.engine.EncounterProvider;

import java.util.List;

public class GoblinAmbushEncounter implements EncounterProvider {
    @Override
    public EncounterCard create() {
        EncounterStage sneakEnd = new EncounterStage(
                "You got away quickly",
                List.of(new StageOption("C", "Continue",null, p -> {}, null, null))
        );
        EncounterStage aftermath = new EncounterStage(
                "The last of the foul cretins lie defeated, and their ill-gotten possesions are now yours",
                List.of(
                        new StageOption(
                                "C", "Collect your spoils", "+ 10 gold",
                                p -> p.addGold(10),   // reward
                                null,                 // then advance the deck
                                null
                        )
                )
        );
        // Root
        EncounterStage root = new EncounterStage(
                "You hear snickering around you in hushed tones. Something nefarious, although not subtle, is skulking in the shadows.",
                List.of(
                        new StageOption("F", "Face the enemy", null,p -> {
                        }, aftermath,
                                new BattleCard("Sneaky Bastards!", List.of(new GoblinCard(), new GoblinCard(), new GoblinCard()))
                        ),
                        new StageOption("S", "quickly sprint in the opposite direction", null,p -> {
                        }, sneakEnd, null)
                )
        );
        return new EncounterCard("Goblin Ambush", root);
    }
}
