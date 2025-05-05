package com.wimbledor.combat.aiBrains;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultAiBehavior implements AiBehavior {
    Random rand;
    @Override
    public Decision decide(ICombatEntity self, List<ICombatEntity> foes) {
        // 1) pick a random available action
        List<ICombatAction> actions = self.getAvailableActions();
        ICombatAction action = actions.get(rand.nextInt(actions.size()));

        // 2) choose targets based on its TargetMode
        List<ICombatEntity> targets;
        switch (action.getTargetMode()) {
            case SELF:
                // heals/buffs self
                targets = List.of(self);
                break;
            case ALL_ENEMIES:
                // AoE: target every foe
                targets = new ArrayList<>(foes);
                break;
            case SINGLE_ENEMY:
            default:
                // single-target: pick one random foe
                ICombatEntity foe = foes.get(rand.nextInt(foes.size()));
                targets = List.of(foe);
                break;
        }

        return new Decision(action, targets);
    }
}

