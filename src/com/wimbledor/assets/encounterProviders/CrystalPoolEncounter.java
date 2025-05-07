// src/com/wimbledor/assets/encounterProviders/CrystalPoolEncounter.java
package com.wimbledor.assets.encounterProviders;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.assets.encounters.StageOption;
import com.wimbledor.entities.monsters.Kappa;
import com.wimbledor.engine.EncounterProvider;

import java.util.List;

public class CrystalPoolEncounter implements EncounterProvider {

    @Override
    public EncounterCard create() {
        // Aftermath stage: no effect, just continue
        EncounterStage end = new EncounterStage(
                "You gather your courage and move on, the dungeon’s mysteries awaiting.",
                List.of(new StageOption(
                        "C", "Continue", null,
                        /* no effect */ null,
                        /* nextStage */ null,
                        /* nextBattle */ null
                ))
        );

        // DRINK branch
        EncounterStage drink3 = new EncounterStage(
                "At the bottom you find a glinting ring—magic? You pocket it and push on.",
                List.of(new StageOption(
                        "C", "Continue and leave pool",
                        /* effectDescription */ "You found a shiny ring!",
                        /* effect */ p -> p.addGold(5),
                        /* nextStage */ end,
                        /* nextBattle */ null
                ))
        );

        EncounterStage drink2a = new EncounterStage(
                "The water is shallow. You spot something shiny beneath the stones.",
                List.of(
                        new StageOption(
                                "S", "Search the bottom",
                                /* no immediate effect */ null,
                                /* effect */ null,
                                /* nextStage */ drink3,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "R", "Rest by the pool’s edge",
                                /* effectDescription */ "You rest and recover 3 HP.",
                                /* effect */ p -> p.heal(3),
                                /* nextStage */ end,
                                /* nextBattle */ null
                        )
                )
        );

        EncounterStage drink2b = new EncounterStage(
                "The ceiling’s crystals reflect moonlight in dazzling patterns.",
                List.of(
                        new StageOption(
                                "M", "Marvel at the patterns",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ end,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "C", "Continue on your way",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ end,
                                /* nextBattle */ null
                        )
                )
        );

        EncounterStage drink1 = new EncounterStage(
                "The water tastes cool and sweet—your head feels clearer.",
                List.of(
                        new StageOption(
                                "E", "Explore the bottom",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ drink2a,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "L", "Lie down and rest",
                                /* effectDescription */ "You rest and recover 2 HP.",
                                /* effect */ p -> p.heal(2),
                                /* nextStage */ drink2b,
                                /* nextBattle */ null
                        )
                )
        );

        // SWIM branch
        EncounterStage swim3 = new EncounterStage(
                "Gasping, you surface. The creatures retreat into the depths.",
                List.of(new StageOption(
                        "C", "Continue",
                        /* effectDescription */ "You collect 8 gold from the pool's edge.",
                        /* effect */ p -> p.addGold(8),
                        /* nextStage */ end,
                        /* nextBattle */ null
                ))
        );
        BattleCard poolBattle = new BattleCard(
                "Underwater assault!",
                List.of(new Kappa())
        );
        EncounterStage swim2 = new EncounterStage(
                "The cooling water washes over your body, renewing your spirit. Then you see a shadow in the blurry water that you can't quite make out.",
                List.of(new StageOption(
                        "F", "Fight them off",
                        /* no immediate effect */ null,
                        /* effect */ null,
                        /* nextStage */ swim3,
                        /* nextBattle */ poolBattle
                ))
        );
        EncounterStage swim1 = new EncounterStage(
                "You wade in … water wraps around your waist. It feels cool.",
                List.of(
                        new StageOption(
                                "D", "Dive deeper",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ swim2,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "S", "Step back ashore",
                                /* effectDescription */ "You step ashore and gain 1 HP.",
                                /* effect */ p -> p.heal(1),
                                /* nextStage */ end,
                                /* nextBattle */ null
                        )
                )
        );

        // AVOID branch
        EncounterStage avoid1 = new EncounterStage(
                "You skirt the pool and hurry onward, uneasy about what lurks below.",
                List.of(new StageOption(
                        "C", "Continue",
                        /* no effect */ null,
                        /* effect */ null,
                        /* nextStage */ end,
                        /* nextBattle */ null
                ))
        );

        // Root stage
        EncounterStage root = new EncounterStage(
                "It’s damp. The room is full of moisture, and you feel your clothes clinging to you as you walk. Ahead, a crystal‐clear pool glimmers.",
                List.of(
                        new StageOption(
                                "D", "Drink from the pool",
                                /* effectDescription */ "You drink and regain 5 HP.",
                                /* effect */ p -> p.heal(5),
                                /* nextStage */ drink1,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "S", "Swim in the pool",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ swim1,
                                /* nextBattle */ null
                        ),
                        new StageOption(
                                "A", "Steer clear and hurry on",
                                /* no effect */ null,
                                /* effect */ null,
                                /* nextStage */ avoid1,
                                /* nextBattle */ null
                        )
                )
        );

        return new EncounterCard("Crystal Pool", root);
    }
}