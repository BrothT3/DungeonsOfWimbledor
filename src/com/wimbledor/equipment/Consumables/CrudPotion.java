package com.wimbledor.equipment.Consumables;

import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;
import com.wimbledor.equipment.Consumable;

import java.util.Random;

/**
 * A cheap, unpredictable healing potion:
 * • Restores 5 HP
 * • 20% chance to reduce either the user's base Attack or base Defense by 1
 */
public class CrudPotion extends Consumable {
    private static final Random RNG = new Random();

    @Override
    public String getName() {
        return "Crud Potion";
    }

    @Override
    public String getDescription() {
        return "The vial is too murky to determine the contents, but maybe you're lucky. "
                + "(Restores 5 HP, but has a 20% chance to reduce Attack or Defense by 1.)";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SELF;
    }

    @Override
    public void modifyStats(ICombatEntity actor, java.util.List<ICombatEntity> targets) {
        // No temporary stat tweaks before execution
    }

    @Override
    public double getDurationSeconds() {
        return 0;
    }

    @Override
    public void execute(ICombatEntity actor, ICombatEntity target) {
        // Only allow self-use
        if (actor.equals(target)) {
            applyEffect(actor);
        }
    }

    @Override
    public void applyEffect(ICombatEntity user) {
        // Heal 5 HP through the common interface
        user.heal(5);

        // 20% chance to debuff a base stat on the Player
        if (RNG.nextFloat() < 0.20f && user instanceof Player p) {
            if (RNG.nextBoolean()) {
                // Decrease base attack but not below 1
//                p.setBaseAttack(Math.max(1, p.getBaseAttack() - 1));
//            } else {
//                // Decrease base defense but not below 0
//                p.setBaseDefense(Math.max(0, p.getBaseDefense() - 1));
            }
        }
    }
}
