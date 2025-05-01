package GameWorld.Equipment;

import GameWorld.Player;

public abstract class Armor implements IEquipment {
    public abstract int getDefenseBonus();
    public int getMaxHPBonus() { return 0; }
    public int getEvasionBonus() { return 0; }
    public int getSpeedBonus() { return 0; }
    public String getName() { return this.getClass().getSimpleName(); }
    public abstract String getDescription();
    @Override
    public void applyToPlayer(Player player) {
        player.setDefense(player.getDefense() + getDefenseBonus());
        player.setMaxHP(player.getMaxHP() + getMaxHPBonus());
        player.setEvasion(player.getEvasion() + getEvasionBonus());
        player.setSpeed(player.getSpeed() + getSpeedBonus());
    }
}
