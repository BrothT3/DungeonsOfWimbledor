package com.wimbledor.equipment.weapons;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.combat.CombatActions.concrete.FerociousSwing;
import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.equipment.Weapon;

import java.util.List;

public class FierceAxe extends Weapon {
    public FierceAxe() {
        super(
                "Fierce Axe",   // name
                5,                // attackBonus
                5,                // penetrationBonus
                10,                // accuracyBonus
                -5,               // speedBonus (slows you slightly)
                List.of(new Slash(1),
                new FerociousSwing(3))         // no special combat actions yet
        );
    }
}

