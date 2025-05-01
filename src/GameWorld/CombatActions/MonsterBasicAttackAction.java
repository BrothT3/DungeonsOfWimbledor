package GameWorld.CombatActions;

import GameWorld.CombatUtils;
import GameWorld.Enums.TargetMode;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;

import java.util.List;

public class MonsterBasicAttackAction implements ICombatAction {
    @Override
    public String getLabel() {
        return "Bite";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.SINGLE_ENEMY;
    }

    @Override
    public void execute(ICombatEntity actor, List<ICombatEntity> targets) {
        for (var t : targets) {
            CombatUtils.entityTurn(actor, t);
        }
        }
}
