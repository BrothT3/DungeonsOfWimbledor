package GameWorld.CombatActions;
import GameWorld.*;
import GameWorld.Enums.TargetMode;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;

import java.util.List;

public class Slash implements ICombatAction {
    @Override public String getLabel() { return "Slash"; }
    @Override public TargetMode getTargetMode() { return TargetMode.SINGLE_ENEMY; }

    @Override
    public void execute(ICombatEntity source, List<ICombatEntity> targets) {
        for (var t : targets) {
            CombatUtils.entityTurn(source, t);
        }
    }
}
