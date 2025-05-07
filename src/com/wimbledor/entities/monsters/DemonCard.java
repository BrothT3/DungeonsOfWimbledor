// src/com/wimbledor/cards/MonsterCards/DemonCard.java
package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.MagicMissile;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

/**
 * Demon encounter: flavor + combat stats/actions + rewards.
 */
public class DemonCard extends BaseNPC {
    public DemonCard() {
        super("Demon",
                        Team.ENEMY,
                        new BaseStats(12, 12, 14, 16, 18, 12),
                        15,
                        25,
                        List.of(new MagicMissile())
                );
    }

    @Override
    public String getRevealText() {
        return "This room feels like an oven, and when you see the fearsome Demon within it, you know why.";
    }

    @Override
    public String getHoverText() {
        return "A horned fiend wreathed in flames—defeat it or get burned.";
    }


}
