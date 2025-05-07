package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class Kappa extends BaseNPC {
    public Kappa() {
        super(
                "Kappa",
                Team.ENEMY,
                new BaseStats(4, 8, 5, 6, 12, 6),
                3,
                5,
                List.of(new Slash(1))
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
