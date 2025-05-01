package GameWorld;

import Cards.BaseCard;
import javax.swing.SwingWorker;
import java.util.*;
import java.util.function.Consumer;

public class WorldManager {
    private final Stack<BaseCard> deck = new Stack<>();
    private int currentLevel = 1;
    private final Random random = new Random();
    private BaseCard currentCard;

    // --- synchronous deck generator (unchanged) ---
    public void generateDeck() {
        deck.clear();
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                deck.push(CardFactory.createMonsterCard(currentLevel));
            } else {
                deck.push(CardFactory.createRandomEventCard(currentLevel));
            }
        }
        Collections.shuffle(deck);
    }

    public void generateDeckAsync(Runnable onComplete) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                generateDeck();
                return null;
            }

            @Override
            protected void done() {
                // runs on EDT
                onComplete.run();
            }
        }.execute();
    }

    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }

    public BaseCard drawCard() {
        if (deck.isEmpty()) return null;
        currentCard = deck.pop();
        return currentCard;
    }

    public BaseCard getCurrentCard() {
        return currentCard;
    }

    public Stack<BaseCard> getDeck() {
        return deck;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public void nextLevel() {
        currentLevel++;
        // you can choose synchronous or async here:
        generateDeck();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
