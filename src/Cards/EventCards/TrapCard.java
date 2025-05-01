package Cards.EventCards;

import Cards.EventCard;
import GameWorld.Player;

public class TrapCard extends EventCard {
    @Override
    public String getText() {
        return """
                You step forward only to hear a click, but you have no time to regret it, you must react quickly!
                Dodge Left: Lose 3 HP
                Dodge Right: Lose 1 ATK
                Jump Up: Lose 1 DEF
                """;
    }

    @Override
    public void applyEffect(Player player, String direction) {
        switch (direction) {
            case "LEFT" -> player.AddHP(-3);
            case "RIGHT" -> player.setAttack(player.getAttack() - 1);
            case "UP" -> player.setDefense(player.getDefense() - 1);
        }
    }
}
