package com.wimbledor.combat.aiBrains;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

import java.util.List;

public class Decision {
    public final ICombatAction action;
    public final List<ICombatEntity> targets;

    public Decision(ICombatAction action, List<ICombatEntity> targets) {
        this.action  = action;
        this.targets = targets;
    }
}