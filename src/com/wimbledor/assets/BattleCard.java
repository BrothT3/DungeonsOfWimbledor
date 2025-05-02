package com.wimbledor.assets;

import com.wimbledor.assets.ICard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Objects;

/**
 * A battle encounter.  Clicking "Fight" will launch the combat engine and
 * keep you on this card until the fight completes.
 */
public class BattleCard implements ICard {
    private final String title;
    private final List<ICombatEntity> monsters;

    public BattleCard(String title, List<ICombatEntity> monsters) {
        this.title    = Objects.requireNonNull(title);
        this.monsters = List.copyOf(Objects.requireNonNull(monsters));
    }

    public List<ICombatEntity> getMonsters() {
        return monsters;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return "Prepare for battle against " + monsters.size() + " foe(s)!";
    }

    @Override
    public List<CardOption> getOptions() {
        // Always show one "Fight" button that:
        //  1) kicks off the combat engine
        //  2) returns *this* so the controller stays in battle mode
        return List.of(new CardOption(
                "F",
                "Fight",
                (Player p) -> GameContext.startBattleWith(p, this),
                this       // ← return this BattleCard, not null
        ));
    }

    @Override
    public ICard onOptionSelected(String code) {
        // Never exit the card on selection; always return self
        return this;
    }

    /**
     * This method will be invoked by GameContext when the fight is truly over.
     * Typically your GameContext.startBattleWith sets the onEncounterComplete()
     * callback, which your controller uses to flip out of battle mode and draw the next card.
     */
}
