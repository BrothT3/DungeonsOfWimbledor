package GameWorld.Equipment;

import GameWorld.Interfaces.ICombatAction;
import GameWorld.Player;

import java.util.List;

public interface IEquipment {
    void applyToPlayer(Player player);
    List<ICombatAction> getActions();
}

