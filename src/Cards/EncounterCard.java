package Cards;

import game.engine.GameContext;
import game.engine.CardDeck;

import java.util.function.Consumer;

public class EncounterCard extends AbstractCard {
    private final EncounterStage rootStage;

    public EncounterCard(EncounterStage rootStage) {
        super(rootStage.getTitle(), rootStage.getDescription());
        this.rootStage = rootStage;
    }

    @Override
    protected void defineOptions() {
        // For each StageOption in the root stage, map it into a CardOption:
        for (StageOption so : rootStage.getOptions()) {
            Consumer<Player> effect = so.getEffect();
            AbstractCard next = null;
            if (so.getNextStage() != null) {
                next = new EncounterCard(so.getNextStage());
            } else if (so.getNextBattle() != null) {
                next = so.getNextBattle(); // BattleCard subclass
            }

            addOption(new CardOption(
                    so.getCode(),
                    so.getLabel(),
                    effect,
                    next
            ));
        }
    }
}