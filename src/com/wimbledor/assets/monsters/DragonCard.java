// src/com/wimbledor/cards/MonsterCards/DragonCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class DragonCard extends BaseNPC {
    public DragonCard() {
        super(
                "Dragon",
                Team.ENEMY,
                /* maxHp */            40,
                /* attack */           12,
                /* defense */           8,
                /* penetration */       6,
                /* accuracy */         90,
                /* evasion */           5,
                /* speed */            10,
                /* critChance */       10,
                /* critMultiplier */    2,
                /* goldReward */       20,
                /* expReward */        30,
                List.of(new Slash())
        );
    }

    @Override
    public String getRevealText() {
        return "A mighty dragon swoops down, its roar shaking the chamber!";
    }

    @Override
    public String getHoverText() {
        return "A legendary beast—its scales glint like molten gold.";
    }
}
