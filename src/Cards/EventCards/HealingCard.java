package Cards.EventCards;

import Cards.EventCard;
import GameWorld.Player;

public class HealingCard extends EventCard {
    @Override
    public String getText() {
        return """
                the remains of an alchemist lie before you
                check what's Left of his healing items: +4 HP
                check the Right pocket for steroids: +1 ATK
                lift Up his cloak and eat a leg for protein: +1 DEF
                """;
    }

    @Override
    public void applyEffect(Player player, String direction) {
        switch (direction) {
            case "LEFT" -> player.AddHP(4);
            case "RIGHT" -> player.setAttack(player.getAttack()+1);
            case "UP" -> player.setDefense(player.getDefense()+1);
        }
    }
}