package GameWorld.Equipment.Consumables;

import GameWorld.Equipment.Consumable;
import GameWorld.Player;

import java.util.Random;

public class CrudPotion extends Consumable {
    private static final Random random = new Random();
    public String getDescription() {
        return "The vial is too murky to determine the contents of it, but hey, maybe you're lucky. (Restores 5 hp, but with a 20% chance of reducing a stat)";
    }
    @Override
    public void applyEffect(Player player) {
        player.AddHP(5);

        // 20% chance to reduce either attack or defense
        if (random.nextFloat() < 0.2f) {
            if (random.nextBoolean()) {
                player.setAttack(Math.max(1, player.getAttack() - 1));
            } else {
                player.setDefense(Math.max(0, player.getDefense() - 1));
            }
        }
    }

    @Override
    public String getName() { return "Crud Potion"; }
}