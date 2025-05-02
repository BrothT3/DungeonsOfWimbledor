package com.wimbledor.equipment;

import com.wimbledor.combat.ICombatAction;
import java.util.ArrayList;
import java.util.List;

public class Armor implements IEquipment {
    private final String name;
    private final int defenseBonus;
    private final int evasionBonus;
    private final int speedBonus;
    private final List<ICombatAction> actions;

    public Armor(String name,
                 int defenseBonus,
                 int evasionBonus,
                 int speedBonus,
                 List<ICombatAction> actions) {
        this.name = name;
        this.defenseBonus = defenseBonus;
        this.evasionBonus = evasionBonus;
        this.speedBonus = speedBonus;
        this.actions = new ArrayList<>(actions);
    }

    @Override public String getName() { return name; }
    @Override public int getAttackBonus() { return 0; }
    @Override public int getDefenseBonus() { return defenseBonus; }
    @Override public int getPenetrationBonus() { return 0; }
    @Override public int getAccuracyBonus() { return 0; }
    @Override public int getEvasionBonus() { return evasionBonus; }
    @Override public int getSpeedBonus() { return speedBonus; }
    @Override public int getCritChanceBonus() { return 0; }
    @Override public int getCritDamageBonus() { return 0; }

    @Override public List<ICombatAction> getActions() {
        return List.copyOf(actions);
    }
}