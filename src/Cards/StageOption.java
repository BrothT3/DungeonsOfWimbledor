package Cards;

import GameWorld.Player;
import java.util.Objects;
import java.util.function.Consumer;
public class StageOption {
    private final String code;
    private final String label;
    private final Consumer<Player> effect;
    private final EncounterStage nextStage;
    private final BattleCard nextBattle;

    public StageOption(String code, String label, Consumer<Player> effect, EncounterStage nextStage, BattleCard nextBattle){
        this.code       = Objects.requireNonNull(code);
        this.label      = Objects.requireNonNull(label);
        this.effect     = effect;
        this.nextStage  = nextStage;
        this.nextBattle = nextBattle;
    }
    public String getCode()            { return code; }
    public String getLabel()           { return label; }
    public EncounterStage getNextStage(){    return  nextStage;    }

    public BattleCard getNextBattle() {    return nextBattle;    }
    public boolean hasEffect()         { return effect != null; }
    public boolean hasNextStage()      { return nextStage != null; }
    public boolean hasNextBattle()     { return nextBattle != null; }

    public void applyEffect(Player player) {
        if (effect != null) effect.accept(player);
    }
}
