// src/com/wimbledor/cards/MonsterCards/OrcCard.java
package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class OrcCard extends BaseNPC {


        public OrcCard() {
            super(
                    "Orc",
                    Team.ENEMY,
                    new BaseStats(14, 10, 12, 12, 6, 8),
                    10,
                    14,
                    List.of(new Slash(1))
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
