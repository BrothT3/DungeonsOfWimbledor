package com.wimbledor.combat.CombatActions;

import com.wimbledor.combat.CombatUtils;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.combat.TargetMode;
import com.wimbledor.entities.ICombatEntity;

/**
 * A basic melee attack.
 */
public class Slash implements ICombatAction {
    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    /**
     * No temporary stat tweaks, so we don’t override modifyStats().
     */

    @Override
    public void execute(ICombatEntity actor, ICombatEntity target) {
        // Use our unified resolveAttack which handles buffs, penetration, crits, etc.
        CombatUtils.resolveAttack(actor, target);
    }
}