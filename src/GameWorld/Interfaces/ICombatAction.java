package GameWorld.Interfaces;

import GameWorld.Enums.TargetMode;

import java.util.List;

public interface ICombatAction {
    String getLabel();
    TargetMode getTargetMode();

    void execute(ICombatEntity source, List<ICombatEntity> targets);
}