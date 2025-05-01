package Cards.EventCards;

import Cards.EventCard;
import GameWorld.Player;

public class LegendaryTreasureCard extends EventCard {
    @Override
    public String getText() {
        return "Legendary treasure uncovered!\nLeft: Equip legendary weapon\nRight: Fully restore HP\nUp: +2 to all stats";
    }

    @Override
    public void applyEffect(Player player, String direction) {
        switch (direction) {
            case "LEFT" -> player.equipLegendaryWeapon();
            case "RIGHT" -> player.restoreFullHealth();
            case "UP" -> player.boostAllStats(2);
        }
    }
}