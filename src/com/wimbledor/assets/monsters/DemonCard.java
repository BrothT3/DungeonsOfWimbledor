// src/com/wimbledor/cards/MonsterCards/DemonCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

/**
 * Demon encounter: flavor + combat stats/actions + rewards.
 */
public class DemonCard extends BaseNPC {
    public DemonCard() {
        super(
                "Demon",            // name
                Team.ENEMY,         // team
                /* maxHp */               25,
                /* attack */               6,
                /* defense */              4,
                /* penetration */          3,
                /* accuracy */             75,
                /* evasion */              10,
                /* speed */                15,
                /* critChance */           20,
                /* critMultiplier */       2,
                /* goldReward */           10,
                /* expReward */            20,
                List.of(new Slash(2))
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

    @Override
    public int getStrength() {
        return 0;
    }

    @Override
    public int getAgility() {
        return 0;
    }

    @Override
    public int getEndurance() {
        return 0;
    }

    @Override
    public int getWillpower() {
        return 0;
    }

    @Override
    public int getKnowledge() {
        return 0;
    }

    @Override
    public int getCunning() {
        return 0;
    }
}
