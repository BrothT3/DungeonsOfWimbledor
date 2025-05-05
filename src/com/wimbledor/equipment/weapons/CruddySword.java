package com.wimbledor.equipment.weapons;

import com.wimbledor.combat.CombatActions.concrete.Slash;
import com.wimbledor.equipment.Weapon;

import java.util.List;

/**
 * this worn sword was the only thing your father left you before he passed. you suspect he didn't love you
 */
public class CruddySword extends Weapon {
    public CruddySword() {
        super(
                "Cruddy Sword",   // name
                4,                // attackBonus
                1,                // penetrationBonus
                5,                // accuracyBonus
                -1,               // speedBonus (slows you slightly)
                List.of(new Slash(1))         // no special combat actions yet
        );
    }
}