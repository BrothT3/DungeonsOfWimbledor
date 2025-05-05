package com.wimbledor.equipment;

import com.wimbledor.combat.CombatActions.ICombatAction;
import java.util.ArrayList;
import java.util.List;

public class Accessory implements IEquipment {
    private final String name;
    private final int critChanceBonus;
    private final int critDamageBonus;
    private final int speedBonus;
    private final List<ICombatAction> actions;

    public Accessory(String name,
                     int critChanceBonus,
                     int critDamageBonus,
                     int speedBonus,
                     List<ICombatAction> actions) {
        this.name = name;
        this.critChanceBonus = critChanceBonus;
        this.critDamageBonus = critDamageBonus;
        this.speedBonus = speedBonus;
        this.actions = new ArrayList<>(actions);
    }

    @Override public String getName() { return name; }
    @Override public int getAttackBonus() { return 0; }
    @Override public int getDefenseBonus() { return 0; }
    @Override public int getPenetrationBonus() { return 0; }
    @Override public int getAccuracyBonus() { return 0; }
    @Override public int getEvasionBonus() { return 0; }
    @Override public int getSpeedBonus() { return speedBonus; }
    @Override public int getCritChanceBonus() { return critChanceBonus; }
    @Override public int getCritDamageBonus() { return critDamageBonus; }

    @Override public List<ICombatAction> getActions() {
        return List.copyOf(actions);
    }
}
