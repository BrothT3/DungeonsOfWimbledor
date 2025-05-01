package Cards;

import GameWorld.Interfaces.ICombatEntity;
import GameWorld.Player;
import GameWorld.TurnManager;
import UI.GameFrame;
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
        // build a TurnManager with player + all the monsters
        List<ICombatEntity> combatants = monsters.stream()
                .map(m -> (ICombatEntity)m)
                .collect(Collectors.toList());
        combatants.add(0, player);

        TurnManager tm = new TurnManager(combatants);
        GameFrame.getInstance().startBattle(this);;
    }

    public List<MonsterCard> getMonsters() {
        return monsters;
    }

    public EncounterStage getPostBattleStage() {
        return postBattleStage;
    }
}
