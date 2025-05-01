package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.Enums.Team;
import GameWorld.Player;

public class SlimeCard extends MonsterCard {
    public SlimeCard() {
        super("Slime",/*hp*/12,   /*atk*/2,  /*def*/6,  /*spd*/5,
                /*acc*/70, /*eva*/0,  /*pen*/2,  /*count*/1,
                /*crit%*/0,/*crit×*/1.5f,
                /*gold*/1);
    }

    @Override
    public String getText() {
        return "You approach a bubbling, foul-smelling ooze. Suddenly it starts moving!";
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