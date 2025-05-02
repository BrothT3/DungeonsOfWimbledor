package UI;

import Cards.*;
import game.cards.EncounterCard;
import game.cards.ICard;
import game.engine.CardDeck;
import game.engine.CardFactory;
import game.engine.GameContext;

import javax.swing.*;
import java.util.List;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Dungeons of WimbleDor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        // 1) Build your raw cards via old CardFactory
        List<ICard> raw = CardFactory.generateAllCards(GameContext.getDungeonLevel());

        // 2) Wrap in a deck
        CardDeck deck = new CardDeck(raw);

        // 3) Instantiate view + controller
        CardView view = new CardView();
        CardController controller = new CardController(deck, view);

        // 4) Lay out UI
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(view, BorderLayout.CENTER);
        // (re-add stats, equipment panels around as needed)

        setVisible(true);

        // 5) Start the run
        controller.start();
    }
}