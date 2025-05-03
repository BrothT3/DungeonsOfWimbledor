// src/com/wimbledor/cards/MonsterCards/GoblinCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Backstab;
import com.wimbledor.combat.CombatActions.Slash;
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
                List.of(new Slash(), new Backstab())
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
}
