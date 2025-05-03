package com.wimbledor.assets.encounterProviders;

import com.wimbledor.assets.BattleCard;
import com.wimbledor.assets.encounters.EncounterCard;
import com.wimbledor.assets.encounters.EncounterStage;
import com.wimbledor.assets.encounters.StageOption;
import com.wimbledor.assets.monsters.Kappa;
import com.wimbledor.engine.EncounterProvider;

import java.util.List;

public class CrystalPoolEncounter implements EncounterProvider {

    @Override
    public EncounterCard create() {
        EncounterStage end = new EncounterStage(
                "You gather your courage and move on, the dungeon’s mysteries awaiting.",
                List.of(new StageOption("C", "Continue", p -> {
                }, null, null))
        );

        // DRINK branch
        EncounterStage drink3 = new EncounterStage(
                "At the bottom you find a glinting ring—magic? You pocket it and push on.",
                List.of(new StageOption("C", "Continue and leave pool", p -> p.addGold(5), end, null))
        );
        EncounterStage drink2a = new EncounterStage(
                "The water is shallow. You spot something shiny beneath the stones.",
                List.of(
                        new StageOption("S", "Search the bottom", p -> {
                        }, drink3, null),
                        new StageOption("R", "Rest by the pool’s edge", p -> p.heal(3), end, null)
                )
        );
        EncounterStage drink2b = new EncounterStage(
                "The ceiling’s crystals reflect moonlight in dazzling patterns.",
                List.of(
                        new StageOption("M", "Marvel at the patterns", p -> {
                        }, end, null),
                        new StageOption("C", "Continue on your way", p -> {
                        }, end, null)
                )
        );
        EncounterStage drink1 = new EncounterStage(
                "The water tastes cool and sweet—your head feels clearer.",
                List.of(
                        new StageOption("E", "Explore the bottom", p -> {
                        }, drink2a, null),
                        new StageOption("L", "Lie down and rest", p -> p.heal(2), drink2b, null)
                )
        );

        // SWIM branch
        // Battle then swim3
        EncounterStage swim3 = new EncounterStage(
                "Gasping, you surface. The creatures retreat into the depths.",
                List.of(new StageOption("C", "Continue", p -> p.addGold(8), end, null))
        );
        BattleCard poolBattle = new BattleCard("Underwater assault!",
                List.of(new Kappa())
        );
        EncounterStage swim2 = new EncounterStage(
                "the cooling water washes over your body, renewing your spirit. Then you see a shadow in the blurry water, that you can't quite make out",
                List.of(new StageOption("F", "Fight them off", p -> {
                }, swim3, poolBattle))
        );
        EncounterStage swim1 = new EncounterStage(
                "You wade in … water wraps around your waist. It feels cool.",
                List.of(
                        new StageOption("D", "Dive deeper", p -> {
                        }, swim2, null),
                        new StageOption("S", "Step back ashore", p -> p.heal(1), end, null)
                )
        );

        // AVOID branch
        EncounterStage avoid1 = new EncounterStage(
                "You skirt the pool and hurry onward, uneasy about what lurks below.",
                List.of(new StageOption("C", "Continue", p -> {
                }, end, null))
        );
        // Root
        EncounterStage root = new EncounterStage(
                "It’s damp. The room is full of moisture, and you feel your clothes clinging to you as you walk. Ahead, a crystal‐clear pool glimmers.",
                List.of(
                        new StageOption("D", "Drink from the pool", p -> p.heal(5), drink1, null),
                        new StageOption("S", "Swim in the pool", p -> {
                        }, swim1, null),
                        new StageOption("A", "Steer clear and hurry on", p -> {
                        }, avoid1, null)
                )
        );


        return new EncounterCard("Crystal Pool", root);
    }
}


