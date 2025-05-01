package Cards;

import GameWorld.Player;
import UI.GameFrame;
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
        // 1) find the matching option
        StageOption opt = current.getOptions().stream()
                .filter(o -> o.getCode().equals(choiceCode))
                .findFirst()
                .orElse(null);
        if (opt == null) return;

        // 2) apply any stat/item effect immediately
        opt.applyEffect(player);

        // 3) if this choice spawns a battle, hand off BOTH parent + battle
        if (opt.hasNextBattle()) {
            BattleCard battle = opt.getNextBattle();
            // remember where to return when fight is over
            battle.setPostBattleStage(opt.getNextStage());
            // explicitly pass 'this' parent encounter into your GameFrame
            GameFrame.getInstance().startBattle(battle);
            return;
        }

        // 4) otherwise if there’s a next stage, advance and refresh UI
        if (opt.hasNextStage()) {
            current = opt.getNextStage();
            GameFrame.getInstance().updateUI();
            return;
        }

        // 5) no more stages or battles → encounter is done
        GameFrame.getInstance().onEncounterComplete();
    }

    public void advanceTo(EncounterStage stage) {
        this.current = stage;
    }

    public void reset() {
        this.current = root;
    }
}