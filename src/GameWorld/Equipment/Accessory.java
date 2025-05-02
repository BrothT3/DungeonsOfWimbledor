package GameWorld.Equipment;

import GameWorld.Interfaces.IEquipment;
import GameWorld.Player;

public abstract class Accessory implements IEquipment {
    public void applyPassiveEffect(Player player) {}
    public boolean canUseAction() { return false; }
    public void useAction(Player player) {}
    public String getName() { return this.getClass().getSimpleName(); }
    public abstract String getDescription();
    @Override
    public void applyToPlayer(Player player) {
        applyPassiveEffect(player);
    }
}
