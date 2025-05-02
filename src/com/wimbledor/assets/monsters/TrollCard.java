// src/com/wimbledor/cards/MonsterCards/TrollCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class TrollCard extends BaseNPC {
    public TrollCard() {
        super(
                "Troll",
                Team.ENEMY,
                /* maxHp */            30,
                /* attack */           10,
                /* defense */           8,
                /* penetration */       2,
                /* accuracy */         65,
                /* evasion */           5,
                /* speed */             4,
                /* critChance */       10,
                /* critMultiplier */    2,
                /* goldReward */        8,
                /* expReward */        12,
                List.of(new Slash())
        );
    }

    @Override
    public String getRevealText() {
        return "A stony troll lumbers forward, regenerating wounds as it moves.";
    }

    @Override
    public String getHoverText() {
        return "Trolls heal quickly—strike hard and fast!";
    }
}
