package com.wimbledor.equipment;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.enums.TargetMode;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

/**
 * A one-time item you can use in combat or encounters.
 * Implements ICombatAction so it slots straight into getAvailableActions().
 */
public abstract class Consumable implements ICombatAction {
    /**
     * Player-friendly name (by default the class name)
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Consumables always target self by default
     */
    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SELF;
    }

    /**
     * This hook lets a consumable tweak stats before execution:
     * e.g. a bomb might temporarily +20% attack before exploding.
     * Default is no-op.
     */
    @Override
    public void modifyStats(ICombatEntity actor, List<ICombatEntity> targets) {
        // no-op; override if your consumable grants a temporary buff
    }

    /**
     * When executed in combat, we simply apply the effect to the user.
     * Outside of combat (e.g. in encounters) you can still call applyEffect().
     */
    @Override
    public void execute(ICombatEntity actor, ICombatEntity target) {
        // We only allow self-targeting
        if (actor.equals(target)) {
            applyEffect(actor);
        }
    }

    /**
     * The actual effect of the consumable (heal, buff, damage, etc.).
     *
     * @param user the entity consuming this item
     */
    public abstract void applyEffect(ICombatEntity user);

    /**
     * Description for tooltips or inventory screens
     */

}