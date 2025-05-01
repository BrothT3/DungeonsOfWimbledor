package GameWorld.Equipment.Accessories;

import GameWorld.Equipment.Accessory;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Player;

import java.util.List;

public class CrudWithString extends Accessory {
    @Override
    public void applyPassiveEffect(Player player) {
        // No effect for now, but structure supports future expansion
    }

    @Override
    public String getName() { return "Crud With String"; }

    @Override
    public String getDescription() {
        return "this is just some crud with a string in it. Adds 10 to apathy (it doesn't)";
    }

    @Override
    public List<ICombatAction> getActions() {
        return List.of();
    }
}
