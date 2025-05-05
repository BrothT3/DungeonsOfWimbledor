// src/com/wimbledor/cards/MonsterCards/DragonCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.concrete.Slash;
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
                List.of(new Slash(1))
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
