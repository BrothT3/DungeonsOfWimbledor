package com.wimbledor.assets.monsters;

import com.wimbledor.combat.CombatActions.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class Kappa extends BaseNPC {
    public Kappa() {
        super(
                "Kappa",
                Team.ENEMY,
                /* maxHp */             15,
                /* attack */             4,
                /* defense */            2,
                /* penetration */        0,
                /* accuracy */          75,
                /* evasion */           10,
                /* speed */              8,
                /* critChance */         5,
                /* critMultiplier */     2,
                /* goldReward */         3,
                /* expReward */          5,
                List.of(new Slash())
        );
    }

    @Override
    public String getRevealText() {
        return "a small reptilian thing clicks it's beak at you!";
    }

    @Override
    public String getHoverText() {
        return "a water-dwelling small man with turtle-like features. Loves to drown unwatched children";
    }
}
