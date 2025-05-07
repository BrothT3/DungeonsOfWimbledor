// src/com/wimbledor/cards/MonsterCards/SlimeCard.java
package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class SlimeCard extends BaseNPC {
    public SlimeCard() {
        super(
                "Slime",
                Team.ENEMY,
                new BaseStats(3, 5, 3, 2, 2, 1),
                2,
                3,
                List.of(new Slash(1))
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
