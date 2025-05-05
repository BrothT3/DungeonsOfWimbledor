package com.wimbledor.equipment.Consumables;

import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.equipment.Consumable;

public class SilverAcorn extends Consumable {
    @Override
    public void applyEffect(ICombatEntity user) {

    }

    @Override
    public String getDescription() {
        return "an acorn made of pure silver. You can feel the weight of the metal, confirming it's authenticity";
    }

    @Override
    public String getLogMessage(ICombatEntity actor, ICombatEntity target, boolean hit, boolean crit, int amount) {
        return super.getLogMessage(actor, target, hit, crit, amount);
    }

    @Override
    public double getDurationSeconds() {
        return 0;
    }
}
