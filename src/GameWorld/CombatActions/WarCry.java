package GameWorld.CombatActions;

import GameWorld.CombatUtils;
import GameWorld.Enums.TargetMode;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;

import java.util.List;

public class WarCry implements ICombatAction {
    @Override
    public String getLabel() {
        return "War Cry";
    }

    @Override
    public TargetMode getTargetMode() {
        return TargetMode.ALL_ENEMIES;
    }

    @Override
    public void execute(ICombatEntity source, List<ICombatEntity> targets) {
        for (ICombatEntity t : targets) {
            CombatUtils.entityTurn(source, t);
        }
    }
}