package Cards;

import GameWorld.Player;

public abstract class EventCard extends BaseCard {
    public abstract void applyEffect(Player player, String direction);

    @Override
    public void onInteract(Player player, String direction) {
        applyEffect(player, direction);
    }
}