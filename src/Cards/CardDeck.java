package Cards;



import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    private final LinkedList<ICard> deck = new LinkedList<>();

    public CardDeck(List<ICard> cards) {
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public boolean hasNext() {
        return !deck.isEmpty();
    }

    public ICard draw() {
        return deck.pollFirst();
    }
}