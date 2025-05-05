// src/com/wimbledor/cards/MonsterCards/GoblinCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.concrete.Backstab;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class GoblinCard extends BaseNPC {
    public GoblinCard() {
        super(
                "Goblin",
                Team.ENEMY,
                /* maxHp */             15,
                /* attack */             4,
                /* defense */            2,
                /* penetration */        1,
                /* accuracy */          75,
                /* evasion */           10,
                /* speed */              8,
                /* critChance */         15,
                /* critMultiplier */     2,
                /* goldReward */         3,
                /* expReward */          5,
                List.of(new Slash(1), new Backstab())
        );
    }

    @Override
    public String getRevealText() {
        return "A sneering goblin leaps out, brandishing a crude dagger!";
    }

    @Override
    public String getHoverText() {
        return "Small and nimble, these goblins swarm in numbers.";
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
