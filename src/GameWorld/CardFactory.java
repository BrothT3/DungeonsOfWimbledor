package GameWorld;

import Cards.*;
import Cards.MonsterCards.*;
import Cards.EventCards.*;

import java.util.*;

public class CardFactory {
    private static final Random random = new Random();

    public static BattleCard createBattleCard(int level) {
        return switch(level) {
            case 1 -> new BattleCard(List.of(new GoblinCard(), new SlimeCard()));
            case 2 -> new BattleCard(List.of(new OrcCard(), new OrcCard()));
            default -> new BattleCard(List.of(new DragonCard()));
        };
    }
    public static BaseCard createEncounterCard(int level) {
        // Stage 2: reward or penalty stage
        EncounterStage rewardStage = new EncounterStage(
                "Aftermath",
                "You found 5 gold!",
                List.of(
                        new StageOption("C","Continue", p->p.addGold(5), null, null)
                )
        );

        // Stage 1: introductory choice, can lead to a battle
        BattleCard fight = createBattleCard(level);
        EncounterStage intro = new EncounterStage(
                "Forest Ambush",
                "You step into the clearing and... something moves!",
                List.of(
                        new StageOption("F","Fight",   null, null, fight),
                        new StageOption("R","Run Away", p->p.setEvasion(p.getEvasion()+10), rewardStage, null)
                )
        );

        return new EncounterCard(intro);
    }

    public static BaseCard createMonsterCard(int level) {
        return switch (level) {
            case 1 -> switch (random.nextInt(2)) {
                case 0 -> new GoblinCard();
                case 1 -> new SlimeCard();
                default -> new SlimeCard();
            };
            case 2 -> switch (random.nextInt(2)) {
                case 0 -> new OrcCard();
                case 1 -> new TrollCard();
                default -> new OrcCard();
            };
            case 3 -> switch (random.nextInt(2)) {
                case 0 -> new DragonCard();
                case 1 -> new DemonCard();
                default -> new DragonCard();
            };
            default -> new SlimeCard();
        };
    }

    public static BaseCard createRandomEventCard(int level) {
        List<BaseCard> events = switch (level) {
            case 1 -> List.of(
                    new HealingCard(),
                    new TrapCard(),
                    new StatSwapCard(),
                    new TreasureCard()
            );
            case 2 -> List.of(
                    // new GreaterHealingCard(),
                    // new DeadlyTrapCard(),
                    // new PowerBoostCard(),
                    // new EquipmentCard()
            );
            case 3 -> List.of(
                    // new GreaterHealingCard(),
                    // new DeadlyTrapCard(),
                    //  new PowerBoostCard(),
                    //  new CurseCard(),
                    new LegendaryTreasureCard()
            );
            default -> List.of(new HealingCard());
        };
        return events.get(random.nextInt(events.size()));
    }
}
