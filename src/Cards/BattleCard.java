package Cards;

import game.engine.GameContext;
import game.engine.CardDeck;
import game.combat.TurnManager;

import java.util.List;

public class BattleCard extends AbstractCard {
    private final List<MonsterCard> monsters;

    public BattleCard(List<MonsterCard> monsters) {
        super("Battle!", "You face: " + monsters);
        this.monsters = monsters;
    }

    @Override
    protected void defineOptions() {
        // Single “Fight” button that kicks off combat
        addOption(new CardOption("F", "Fight", player -> {
            TurnManager tm = new TurnManager(player, monsters);
            tm.startBattle();
        }, null));
    }
}