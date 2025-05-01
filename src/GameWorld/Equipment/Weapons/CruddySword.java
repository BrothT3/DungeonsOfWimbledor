package GameWorld.Equipment.Weapons;

import GameWorld.CombatActions.Slash;
import GameWorld.Equipment.Weapon;
import GameWorld.Interfaces.ICombatAction;

import java.util.List;

public class CruddySword extends Weapon {
    @Override
    public int getAttackBonus() {
        return 1;
    }

    @Override
    public String getName() {
        return "Cruddy Sword";
    }
    public String getDescription() {
        return "This sword was all your father left you when he passed away. you suspect he didn't really love you (Adds +1 ATK.)";
    }
    @Override
    public List<ICombatAction> getActions() {
        return List.of(new Slash());
    }
}