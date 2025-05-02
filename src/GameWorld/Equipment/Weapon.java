package GameWorld.Equipment;

import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.IEquipment;
import GameWorld.Player;

import java.util.List;

public abstract class Weapon implements IEquipment {
    public abstract int getAttackBonus();
    public int getAttackCountBonus() { return 0; }
    public int getPenetrationBonus() { return 0; }
    public String getName() { return this.getClass().getSimpleName(); }
    public abstract String getDescription();
    public List<ICombatAction> getActions() {
        return List.of();
    }
    @Override
    public void applyToPlayer(Player player) {
        player.setAttack(player.getAttack() + getAttackBonus());
        player.setAttackCount(player.getAttackCount() + getAttackCountBonus());
        player.setPenetration(player.getPenetration() + getPenetrationBonus());
    }
}
