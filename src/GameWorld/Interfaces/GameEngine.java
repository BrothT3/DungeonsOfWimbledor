package GameWorld.Interfaces;

import Cards.BattleCard;
import GameWorld.TurnManager;

public interface GameEngine {
    /** Swap into a battle: given a BattleCard and its TurnManager. */
    void startBattle(BattleCard battle, TurnManager turnManager);

    /** Advance the current encounter to its next stage (or end it). */
    void onEncounterComplete();

    /** Refresh whatever is on screen to match new state. */
    void refreshUI();
}