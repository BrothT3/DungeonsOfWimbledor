package com.wimbledor.equipment.accessories;

import com.wimbledor.equipment.Accessory;

import java.util.List;

/**
 return "this is just some crud with a string in it. Adds 10 to apathy (it doesn't)"
 * More for flavor than utility.
 */
public class CrudWithString extends Accessory {
    public CrudWithString() {
        super(
                "Crud With String", // name
                0,                  // critChanceBonus
                0,                  // critDamageBonus
                0,                  // speedBonus
                List.of()           // no extra abilities
        );
    }
}


