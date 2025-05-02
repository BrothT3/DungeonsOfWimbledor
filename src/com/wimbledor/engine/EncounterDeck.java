// src/com/wimbledor/engine/EncounterDeck.java
package com.wimbledor.engine;

import com.wimbledor.assets.encounters.EncounterCard;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * A deck of narrative encounters only.
 * Never contains BattleCards or NPCs directly.
 */
public class EncounterDeck {
    private final Deque<EncounterCard> deck = new ArrayDeque<>();

    /**
     * Build and shuffle your encounter run.
     *
     * @param encounters the list of fully‐constructed EncounterCard instances
     */
    public EncounterDeck(List<EncounterCard> encounters) {
        Collections.shuffle(encounters);
        deck.addAll(encounters);
    }

    /**
     * Are there more encounters in this run?
     */
    public boolean hasNext() {
        return !deck.isEmpty();
    }

    /**
     * Draw the next EncounterCard (or null if the deck is empty).
     */
    public EncounterCard draw() {
        return deck.pollFirst();
    }
}
