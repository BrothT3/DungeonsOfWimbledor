// src/com/wimbledor/cards/MonsterCards/GoblinCard.java
package com.wimbledor.entities.monsters;

import com.wimbledor.combat.BaseStats;
import com.wimbledor.combat.CombatActions.concrete.Backstab;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.entities.BaseNPC;
import com.wimbledor.entities.Team;

import java.util.List;

public class GoblinCard extends BaseNPC {
    public GoblinCard() {
        super(
                "Goblin",
                Team.ENEMY,
                /* Strength */             new BaseStats(6, 12, 7, 4, 4, 12),
                /* Goldreward */          5,
                /* expreward*/           10,
                List.of(new Slash(1), new Backstab())
        );
    }

    @Override
    public String getRevealText() {
        return "A sneering goblin leaps out, brandishing a crude dagger!";
    }

    @Override
    public String getHoverText() {
        return "Small and nimble, these goblins swarm in numbers.";
    }

    @Override
    public Team getTeam() {
        return Team.ENEMY;
    }
}
