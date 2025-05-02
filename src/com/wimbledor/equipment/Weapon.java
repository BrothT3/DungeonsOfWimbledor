package com.wimbledor.equipment;

import com.wimbledor.combat.ICombatAction;
import java.util.ArrayList;
import java.util.List;

public class Weapon implements IEquipment {
    private final String name;
    private final int attackBonus;
    private final int penetrationBonus;
    private final int accuracyBonus;
    private final int speedBonus;
    private final List<ICombatAction> actions;

    public Weapon(String name,
                  int attackBonus,
                  int penetrationBonus,
                  int accuracyBonus,
                  int speedBonus,
                  List<ICombatAction> actions) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.penetrationBonus = penetrationBonus;
        this.accuracyBonus = accuracyBonus;
        this.speedBonus = speedBonus;
        this.actions = new ArrayList<>(actions);
    }

    @Override public String getName() { return name; }
    @Override public int getAttackBonus() { return attackBonus; }
    @Override public int getDefenseBonus() { return 0; }
    @Override public int getPenetrationBonus() { return penetrationBonus; }
    @Override public int getAccuracyBonus() { return accuracyBonus; }
    @Override public int getEvasionBonus() { return 0; }
    @Override public int getSpeedBonus() { return speedBonus; }
    @Override public int getCritChanceBonus() { return 0; }
    @Override public int getCritDamageBonus() { return 0; }

    @Override public List<ICombatAction> getActions() {
        return List.copyOf(actions);
    }
}