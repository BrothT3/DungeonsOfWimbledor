package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.Enums.Team;
import GameWorld.Player;

public class OrcCard extends MonsterCard {
    public OrcCard() {
        super("Orc",/*hp*/15,   /*atk*/8,  /*def*/2,  /*spd*/15,
                /*acc*/60, /*eva*/5,  /*pen*/3,  /*count*/1,
                /*crit%*/15,/*crit×*/1.5f,
                /*gold*/1);
    }

    @Override
    public String getText() {
        return "A brutish orc charges at you with a club!";
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