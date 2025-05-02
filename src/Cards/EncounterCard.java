package Cards;

import GameWorld.Interfaces.ICombatEntity;
import GameWorld.Player;
import GameWorld.TurnManager;
import UI.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A multi‐stage encounter.  Picking an option may:
 *  • apply a stat/item effect,
 *  • immediately branch to a BattleCard,
 *  • or advance to another EncounterStage.
 */
public class EncounterCard extends BaseCard {
    private final EncounterStage root;
    private EncounterStage       current;

    public EncounterCard(EncounterStage root) {
        this.root    = root;
        this.current = root;
    }

    @Override
    public String getTitle() {
        return current.getTitle();
    }

    @Override
    public String getText() {
        String desc = current.getDescription();
        String opts = current.getOptions().stream()
                .map(o -> "[" + o.getCode() + "] " + o.getLabel())
                .collect(Collectors.joining("   "));
        return desc + "\n\n" + opts;
    }
    public EncounterStage getCurrentStage(){
        return current;
    }

    @Override
    public void onInteract(Player player, String choiceCode) {
        StageOption opt = current.getOptions().stream()
                .filter(o -> o.getCode().equals(choiceCode))
                .findFirst().orElse(null);
        if (opt == null) return;

        // 1) apply any immediate stat/item effects
        opt.applyEffect(player);

        // 2) if this choice spawns a battle → hand off to engine
        if (opt.hasNextBattle()) {
            BattleCard battle = opt.getNextBattle();
            battle.setPostBattleStage(opt.getNextStage());
            // prepare its TurnManager:
            List<ICombatEntity> combatants = new ArrayList<>(battle.getMonsters());
            combatants.add(0, player);
            TurnManager tm = new TurnManager(combatants);
            engine.startBattle(battle, tm);
            return;
        }

        // 3) advance to next stage if any
        if (opt.hasNextStage()) {
            current = opt.getNextStage();
            engine.refreshUI();
            return;
        }

        // 4) no more choices → encounter done
        engine.onEncounterComplete();
    }

    public void advanceTo(EncounterStage stage) {
        this.current = stage;
    }

    public void reset() {
        this.current = root;
    }
}