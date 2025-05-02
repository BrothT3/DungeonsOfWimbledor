package Cards;

import GameWorld.Interfaces.GameEngine;
import GameWorld.Player;

public abstract class BaseCard {
    protected GameEngine engine;
    public String getTitle() {
        return this.getClass().getSimpleName().replace("Card", "");
    }
    public void setEngine(GameEngine e) {
        this.engine = e;
    }
    public abstract String getText();

    public abstract void onInteract(Player player, String direction);
}

