package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.Enums.Team;
import GameWorld.Player;

public class GoblinCard extends MonsterCard {
    public GoblinCard() {
        super("Goblin",/*hp*/8,   /*atk*/2,  /*def*/1,  /*spd*/25,
                /*acc*/75, /*eva*/5,  /*pen*/0,  /*count*/1,
                /*crit%*/15,/*crit×*/1.5f,
                /*gold*/1);
    }

    @Override
    public String getText() {
        return "An ugly, small humanoid draws near with a wicked grin!";
    }

    @Override
    public void onInteract(Player player, String direction) {

    }

    @Override
    public void setName(String value) {

    }

    @Override
    public void setTeam(Team value) {

    }
}