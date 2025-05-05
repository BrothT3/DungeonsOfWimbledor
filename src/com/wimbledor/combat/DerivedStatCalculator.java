// src/com/wimbledor/combat/DerivedStatCalculator.java
package com.wimbledor.combat;

import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.entities.ICombatEntity;

public class DerivedStatCalculator {

    public static int compute(ICombatEntity entity, DerivedStat stat) {
        return switch (stat) {
            case MAX_HP -> getMaxHp(entity);
            case CRIT_CHANCE -> getCritChance(entity);
            case CRIT_MULTIPLIER -> getCritMultiplier(entity);
            case SPEED -> getSpeed(entity);
            case ACCURACY -> getAccuracy(entity);
            case EVASION -> getEvasion(entity);
            case DEFENSE -> getDefense(entity);
            case PENETRATION -> getPenetration(entity);
        };
    }

    public static int getMaxHp(ICombatEntity entity) {
        return 50 + entity.getStat(Stat.ENDURANCE) * 2;
    }

    public static int getCritChance(ICombatEntity entity) {
        return 5 + (entity.getStat(Stat.CUNNING) / 2);
    }

    public static int getCritMultiplier(ICombatEntity entity) {
        return 2; // Can be scaled by perks/equipment later
    }

    public static int getSpeed(ICombatEntity entity) {
        return entity.getStat(Stat.AGILITY);
    }

    public static int getAccuracy(ICombatEntity entity) {
        return 75 + entity.getStat(Stat.AGILITY);
    }

    public static int getEvasion(ICombatEntity entity) {
        return 3 + (entity.getStat(Stat.AGILITY) / 3);
    }

    public static int getDefense(ICombatEntity entity) {
        return entity.getStat(Stat.ENDURANCE);
    }

    public static int getPenetration(ICombatEntity entity) {
        return entity.getStat(Stat.CUNNING);
    }

    public static int getMagicResist(ICombatEntity entity) {
        return entity.getStat(Stat.WILLPOWER); // placeholder
    }

    // Optional: add overloaded method to include equipment/status modifiers later
}
