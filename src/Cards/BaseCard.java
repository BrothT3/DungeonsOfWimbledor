package Cards;

import GameWorld.Player;

public abstract class BaseCard {
    public String getTitle() {
        return this.getClass().getSimpleName().replace("Card", "");
    }

    public abstract String getText();

    public String getStats() {
        return "";
    }

    public abstract void onInteract(Player player, String direction);
}

