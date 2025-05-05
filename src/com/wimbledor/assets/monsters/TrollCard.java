// src/com/wimbledor/cards/MonsterCards/TrollCard.java
package com.wimbledor.assets.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class TrollCard extends BaseNPC {
    public TrollCard() {
        super(
                "Troll",
                Team.ENEMY,
                new BaseStats(21, 4, 122, 5, 1, 2),
                8,
                12,
                List.of(new Slash(1))
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
