package Cards.EventCards;

import Cards.EventCard;
import GameWorld.Player;

public class TreasureCard extends EventCard {
    @Override
    public String getText() {
        return "You found a treasure!\nLeft: +5 gold\nRight: +1 random stat\nUp: +1 consumable";
    }

    @Override
    public void applyEffect(Player player, String direction) {
        switch (direction) {
            case "LEFT" -> player.setGold(player.getGold()+5);
            case "RIGHT" -> player.increaseRandomStat();
            case "UP" -> player.addRandomConsumable();
        }
    }
}