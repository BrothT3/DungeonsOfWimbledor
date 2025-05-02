package com.wimbledor.equipment.armors;

import com.wimbledor.equipment.Armor;

import java.util.List;

/**
 * return "These were fine trousers before the previous owner shat, pissed and never washed them. ever. Adds +1 DEF.";
 */
public class CruddyTrousers extends Armor {
    public CruddyTrousers() {
        super(
                "Cruddy Trousers", // name
                2,                 // defenseBonus
                1,                 // evasionBonus (they’re light)
                0,                 // speedBonus
                List.of()          // no reactive actions
        );
    }
}





