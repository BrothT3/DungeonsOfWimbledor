package GameWorld;

import Cards.BaseCard;
import Cards.MonsterCards.*;
import Cards.EventCards.*;

import java.util.*;

public class CardFactory {
    private static final Random random = new Random();

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
