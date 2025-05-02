package Cards;

import GameWorld.Interfaces.ICombatEntity;
import GameWorld.Player;
import GameWorld.TurnManager;
import UI.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps one or more MonsterCards into a single turn‐based fight.
 * When done, returns to its parent EncounterStage.
 */
public class BattleCard extends BaseCard {
    private final List<MonsterCard> monsters;
    private EncounterStage          postBattleStage;
    private EncounterCard parentEncounter;
    public BattleCard(List<MonsterCard> monsters) {
        this.monsters = monsters;
    }

    /** Called by EncounterCard to tell us where to go next. */
    public void setPostBattleStage(EncounterStage stage) {
        this.postBattleStage = stage;
    }
    public void setParentEncounter(EncounterCard encounter) {
        this.parentEncounter = encounter;
    }
    /** so GameFrame can look it up later: */
    public EncounterCard getParentEncounter() {
        return parentEncounter;
    }
    @Override
    public String getTitle() {
        return "Battle!";
    }

    @Override
    public String getText() {
        return monsters.stream()
                .map(m -> m.getTitle() + " (HP:" + m.getHP() + ")")
                .collect(Collectors.joining("\n"));
    }


    @Override
    public void onInteract(Player player, String ignored) {
        // we only ever hit this once, to initiate the battle
        List<ICombatEntity> combatants = new ArrayList<>(monsters);
        combatants.add(0, player);
        TurnManager tm = new TurnManager(combatants);
        engine.startBattle(this, tm);
    }

    public List<MonsterCard> getMonsters() {
        return monsters;
    }

    public EncounterStage getPostBattleStage() {
        return postBattleStage;
    }
}
