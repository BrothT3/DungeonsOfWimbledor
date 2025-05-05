package com.wimbledor.combat.aiBrains;

import com.wimbledor.entities.ICombatEntity;

import java.util.List;

public interface AiBehavior {
    /**
     * Decide the next action + targets, given your own entity and the list of foes.
     */
    Decision decide(ICombatEntity self, List<ICombatEntity> foes);
}