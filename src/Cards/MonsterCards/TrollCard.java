package Cards.MonsterCards;

import Cards.MonsterCard;
import GameWorld.CombatActions.Slash;
import GameWorld.Enums.Team;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Player;

import java.util.List;

public class TrollCard extends MonsterCard {
    public TrollCard() {
        super("Troll",/*hp*/22,   /*atk*/12,  /*def*/3,  /*spd*/3,
                /*acc*/45, /*eva*/0,  /*pen*/6,  /*count*/1,
                /*crit%*/0,/*crit×*/1.5f,
                /*gold*/1);
    }

    @Override
    public String getText() {
        return "A massive lumbering humanoid stumbles across you, while picking it's teeth with a leg-bone. As your eyes meet, it's stomach starts to rumble";
    }
    @Override public void onInteract(Player player, String direction) {    }
    @Override public void setName(String value) {    }
    @Override public void setTeam(Team value) {    }
    @Override
    public void setActions(List<ICombatAction> a) {
        actions = List.of(new Slash());
    }
}
