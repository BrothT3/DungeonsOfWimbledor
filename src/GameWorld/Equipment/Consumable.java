package GameWorld.Equipment;

import GameWorld.Player;

public abstract class Consumable {
    public abstract void applyEffect(Player player);
    public String getName() { return this.getClass().getSimpleName(); }
    public abstract String getDescription();
}
