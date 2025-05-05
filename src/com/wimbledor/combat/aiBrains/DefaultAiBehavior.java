package com.wimbledor.combat.aiBrains;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatActions.StatScalingAttack;
import com.wimbledor.entities.ICombatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultAiBehavior implements AiBehavior {
    private final Random rand = new Random();

    @Override
    public Decision decide(ICombatEntity self, List<ICombatEntity> foes) {
        // 1. Filter to only StatScalingAttack actions (safe for executor)
        List<ICombatAction> actions = self.getAvailableActions()
                .stream()
                .filter(a -> a instanceof StatScalingAttack)
                .toList();

        if (actions.isEmpty() || foes.isEmpty()) {
            return new Decision(null, List.of());
        }

        ICombatAction action = actions.get(rand.nextInt(actions.size()));

        // 2. Select valid targets based on target mode
        List<ICombatEntity> targets;
        switch (action.getTargetMode()) {
            case SELF -> targets = List.of(self);

            case ALL_ENEMIES -> {
                targets = foes.stream().filter(ICombatEntity::isAlive).toList();
                if (targets.isEmpty()) targets = List.of(self); // fallback
            }

            case SINGLE_ENEMY -> {
                List<ICombatEntity> aliveFoes = foes.stream().filter(ICombatEntity::isAlive).toList();
                if (aliveFoes.isEmpty()) return new Decision(null, List.of());
                ICombatEntity foe = aliveFoes.get(rand.nextInt(aliveFoes.size()));
                targets = List.of(foe);
            }

            default -> {
                // Fallback: just attack self if unknown TargetMode
                targets = List.of(self);
            }
        }

        return new Decision(action, targets);
    }
}
