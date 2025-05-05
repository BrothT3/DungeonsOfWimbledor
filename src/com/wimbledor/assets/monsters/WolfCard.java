package com.wimbledor.assets.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Backstab;
import com.wimbledor.combat.CombatActions.concrete.Bite;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class WolfCard extends BaseNPC {
    public WolfCard() {
        super(
                "Wolf",
                Team.ENEMY,
                new BaseStats(6, 15, 8, 3, 2, 5),
                3,
                5,
                List.of(new Bite())
        );
    }
    @Override
    public String getRevealText() {
        return "Snarling, a wolf approaches";
    }

    @Override
    public String getHoverText() {
        return "man's best friend, a feral beast, a symbol of nobility. but currently, your impending death";
    }

}
