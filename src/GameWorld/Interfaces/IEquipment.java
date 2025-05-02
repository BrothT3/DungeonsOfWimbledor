package GameWorld.Interfaces;

import GameWorld.Player;

import java.util.List;

public interface IEquipment {
    void applyToPlayer(Player player);
    List<ICombatAction> getActions();
}

