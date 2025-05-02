package com.wimbledor.assets;

import com.wimbledor.assets.ICard;
import com.wimbledor.assets.CardOption;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a battle encounter composed of multiple combat entities.
 * When selected, it delegates execution to the GameContext to run the battle.
 */
public class BattleCard implements ICard {
    private final String title;
    private final List<ICombatEntity> monsters;

    /**
     * @param title Display name for this battle.
     * @param monsters The list of entities the player will face.
     */
    public BattleCard(String title, List<ICombatEntity> monsters) {
        this.title = Objects.requireNonNull(title);
        this.monsters = new ArrayList<>(Objects.requireNonNull(monsters));
    }

    /**
     * Accessor for the combat engine.
     */
    public List<ICombatEntity> getMonsters() {
        return List.copyOf(monsters);
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
        // Single option to start the battle
        return List.of(new CardOption(
                "F",
                "Fight",
                (Player player) -> GameContext.startBattleWith(player, this),
                null // after battle completes, control returns via callback
        ));
    }

    @Override
    public ICard onOptionSelected(String code) {
        // Only one option; return null to signal next encounter
        return null;
    }
}