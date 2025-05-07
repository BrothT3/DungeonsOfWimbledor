// src/com/wimbledor/cards/MonsterCards/DragonCard.java
package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class DragonCard extends BaseNPC {
    public DragonCard() {
        super(
                "Dragon",
                Team.ENEMY,
                new BaseStats(24, 10, 18, 16, 18, 10),
                20,
                40,
                List.of(new Slash(1))
        );
    }
    @Override
    public String getRevealText() {
        return "A mighty dragon swoops down, its roar shaking the chamber!";
    }

    @Override
    public String getHoverText() {
        return "A legendary beast—its scales glint like molten gold.";
    }


}
