package Cards;

import Cards.ICard;
import GameWorld.Player;

import java.util.function.Consumer;

public class CardOption {
    private final String code;            // unique identifier
    private final String label;           // what to show on button
    private final Consumer<Player> effect;// immediate effect on player
    private final ICard nextCard;         // next card in a sequence (nullable)

    public CardOption(String code, String label,
                      Consumer<Player> effect,
                      ICard nextCard) {
        this.code     = code;
        this.label    = label;
        this.effect   = effect;
        this.nextCard = nextCard;
    }

    public String getCode()        { return code; }
    public String getLabel()       { return label; }
    public ICard getNextCard()     { return nextCard; }

    /** Apply any immediate player-side effect. */
    public void applyEffect(Player player) {
        if (effect != null) effect.accept(player);
    }
}