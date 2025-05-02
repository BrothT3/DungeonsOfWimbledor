package com.wimbledor.equipment;

import com.wimbledor.combat.ICombatAction;
import java.util.List;
public interface IEquipment {
    String getName();

    /** Stat bonus getters: can be zero when not applicable */
    int getAttackBonus();
    int getDefenseBonus();
    int getPenetrationBonus();
    int getAccuracyBonus();
    int getEvasionBonus();
    int getSpeedBonus();
    int getCritChanceBonus();
    int getCritDamageBonus();

    /** Combat actions granted by this equipment */
    List<ICombatAction> getActions();
}
