package Cards.EventCards;

import Cards.EventCard;
import GameWorld.Player;

public class StatSwapCard extends EventCard {
    @Override
    public String getText() {
        return """
                The air in here smells weird, and then your nose starts tingling. Weird magic!
                stick your tongue out to the Left: Swap ATK & DEF
                use your Right hand to breathe more in: Halve HP
                cover Up your nose and rush forward: Gain 8 HP but lose 1 DEF and 1 ATK
                """;
    }

    @Override
    public void applyEffect(Player player, String direction) {
        switch (direction) {
            case "LEFT" -> {
                int temp = player.getAttack();
                player.setAttack(player.getDefense());
                player.setDefense(temp);
            }
            case "RIGHT" -> player.setHP(player.getHP() / 2);
            case "UP" -> {
                player.AddHP(8);
                player.setDefense(player.getDefense() - 1);
                player.setAttack(player.getAttack() - 1);
            }
        }
    }
}
