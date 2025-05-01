package Cards;

import GameWorld.Player;

public abstract class BaseCard {
    public String getTitle() {
        return this.getClass().getSimpleName().replace("Card", "");
    }

    public abstract String getText();

    public abstract void onInteract(Player player, String direction);
}

