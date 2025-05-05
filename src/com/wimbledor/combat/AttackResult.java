// src/com/wimbledor/combat/AttackResult.java
package com.wimbledor.combat;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

/**
 * A single result of executing an ICombatAction on one target.
 */
public record AttackResult(
        ICombatAction  action,
        ICombatEntity  actor,
        ICombatEntity  target,
        boolean        hit,
        boolean        crit,
        int            damage
) {}