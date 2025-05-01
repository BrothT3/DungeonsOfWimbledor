package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.Enums.Team;
import GameWorld.Player;

public class DemonCard extends MonsterCard {
    public DemonCard() {
        super("Demon",/*hp*/25,   /*atk*/6,  /*def*/4,  /*spd*/15,
                /*acc*/75, /*eva*/10,  /*pen*/3,  /*count*/3,
                /*crit%*/20,/*crit×*/1.5f,
                /*gold*/10);
    }
    @Override public String getText() {
        return "This room feels like an oven, and when you see fearsome Demon within it, you know why";
    }
    @Override public void onInteract(Player player, String direction) {

    }
    @Override public void setName(String value) {

    }
    @Override public void setTeam(Team value) {

    }
}