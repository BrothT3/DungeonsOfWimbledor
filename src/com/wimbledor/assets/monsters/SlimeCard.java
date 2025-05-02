// src/com/wimbledor/cards/MonsterCards/SlimeCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class SlimeCard extends BaseNPC {
    public SlimeCard() {
        super(
                "Slime",
                Team.ENEMY,
                /* maxHp */             10,
                /* attack */             3,
                /* defense */            1,
                /* penetration */        0,
                /* accuracy */          70,
                /* evasion */           15,
                /* speed */              5,
                /* critChance */         3,
                /* critMultiplier */     1,
                /* goldReward */         2,
                /* expReward */          3,
                List.of(new Slash())
        );
    }

    @Override
    public String getRevealText() {
        return "You hear a wet plop as a gelatinous slime oozes into view.";
    }

    @Override
    public String getHoverText() {
        return "Weak but sticky—avoid its slow, corrosive touch.";
    }
}
