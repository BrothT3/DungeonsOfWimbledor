package GameWorld;

import Cards.BaseCard;
import Cards.BattleCard;

import java.util.*;

public class WorldManager {
    private final List<BaseCard> deck = new ArrayList<>();
    private int currentLevel = 1;
    private final Random random = new Random();
    private BaseCard currentCard;


    // instead of Deque or Stack, just:


    public void generateDeck() {
        deck.clear();
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                deck.add(CardFactory.createMonsterCard(currentLevel));
            } else if (random.nextBoolean()) {
                deck.add(CardFactory.createBattleCard(currentLevel));
            } else {
                deck.add(CardFactory.createEncounterCard(currentLevel));
            }
        }
        Collections.shuffle(deck);
    }

    public BaseCard drawCard() {
        if (deck.isEmpty()) return null;
        // use LIFO to simulate a stack:
        currentCard = deck.remove(deck.size() - 1);
        return currentCard;
    }

    public List<BaseCard> getDeck() {
        return deck;
    }

    /**
     * Return whatever was last drawn (may be null).
     */
    public BaseCard getCurrentCard() {
        return currentCard;
    }

    /**
     * How many cards remain in the deck.
     */
    public int getDeckSize() {
        return deck.size();
    }

    /**
     * Advance to next level: bump level, regenerate synchronously.
     */
    public void nextLevel() {
        currentLevel++;
        generateDeck();
    }

    /**
     * Reset deck to level 1 and build it immediately.
     */
    public void reset() {
        currentLevel = 1;
        generateDeck();
    }

    /**
     * For UI: expose an unmodifiable view of the remaining deck.
     */
    public List<BaseCard> peekDeck() {
        return Collections.unmodifiableList(new ArrayList<>(deck));
    }
}