package GameWorld.Equipment.Armors;

import GameWorld.Equipment.Armor;
import GameWorld.Interfaces.ICombatAction;

import java.util.List;

public class CruddyTrousers extends Armor {
    @Override
    public int getDefenseBonus() { return 1; }

    @Override
    public int getSpeedBonus() {
        return 50;
    }

    @Override
    public String getName() { return "Cruddy Trousers"; }
    public String getDescription() {
        return "These were fine trousers before the previous owner shat, pissed and never washed them. ever. Adds +1 DEF.";
    }

    @Override
    public List<ICombatAction> getActions() {
        return List.of();
    }
}