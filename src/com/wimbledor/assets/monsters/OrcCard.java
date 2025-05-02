// src/com/wimbledor/cards/MonsterCards/OrcCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class OrcCard extends BaseNPC {
    public OrcCard() {
        super(
                "Orc",
                Team.ENEMY,
                /* maxHp */            20,
                /* attack */            8,
                /* defense */           5,
                /* penetration */       1,
                /* accuracy */         80,
                /* evasion */           7,
                /* speed */             6,
                /* critChance */        7,
                /* critMultiplier */    2,
                /* goldReward */        5,
                /* expReward */         8,
                List.of(new Slash())
        );
    }

    @Override
    public String getRevealText() {
        return "A hulking orc brandishes its greataxe, snarling at you!";
    }

    @Override
    public String getHoverText() {
        return "Orcs are brutal fighters—watch their war cries.";
    }
}
