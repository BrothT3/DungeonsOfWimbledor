package UI;

import Cards.ICard;
import game.engine.CardDeck;
import game.engine.GameContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController implements ActionListener {
    private final CardDeck deck;
    private final CardView view;

    private ICard currentCard;

    public CardController(CardDeck deck, CardView view) {
        this.deck = deck;
        this.view = view;
    }

    /** Kick off the first draw */
    public void start() {
        drawNext();
    }

    private void drawNext() {
        if (deck.hasNext()) {
            currentCard = deck.draw();
            view.display(currentCard, this);
        } else {
            GameContext.onRunComplete();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        ICard next = currentCard.onOptionSelected(code);

        // If the option itself launches a battle, that logic is handled inside the card’s effect.
        // After battle (or if next != null), display next:
        if (next != null) {
            currentCard = next;
            view.display(currentCard, this);
        } else {
            drawNext();
        }
    }
}