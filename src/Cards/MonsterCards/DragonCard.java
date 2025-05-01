package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.Enums.Team;
import GameWorld.Player;

public class DragonCard extends MonsterCard {
    public DragonCard() {
        super("Dragon",/*hp*/40,   /*atk*/12,  /*def*/8,  /*spd*/10,
                /*acc*/90, /*eva*/5,  /*pen*/6,  /*count*/1,
                /*crit%*/10,/*crit×*/1.5f,
                /*gold*/1);

    }

    @Override
    public String getText() {
        return "A mighty dragon blocks your path, its eyes glowing!";
    }

    @Override
    public void onInteract(Player player, String direction) {
        // Interaction logic can be expanded as needed
    }

    @Override
    public void setName(String value) {

    }

    @Override
    public void setTeam(Team value) {

    }
}
