package GameWorld;

import GameWorld.Enums.Team;
import GameWorld.Equipment.Accessory;
import GameWorld.Equipment.IEquipment;
import GameWorld.Equipment.Weapon;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements ICombatEntity {
    private int hp = 20;
    private int maxHP = 20;
    private int attackDamage = 2;
    private int attackCount = 1;
    private int attackPenetration = 0;
    private int defense = 3;
    private int speed = 10;
    private int evasion = 5;
    private int critChance = 5;
    private float critDamage = 1.5f;
    private int accuracy = 80;
    private int gold = 0;
    private int shakes = 3;

    // CombatEntity stuff

    @Override public int getHP() { return hp; }
    @Override public int getMaxHP() { return maxHP; }
    @Override public int getAttack() { return attackDamage; }
    @Override public int getAttackCount() { return attackCount; }
    @Override public int getPenetration() { return attackPenetration; }
    @Override public int getDefense() { return defense; }
    @Override public int getSpeed() { return speed; }
    @Override public int getEvasion() { return evasion; }
    @Override public int getAccuracy() { return accuracy; }
    @Override public int getCritChance() { return critChance; }
    @Override public float getCritDamage() { return critDamage; }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Team getTeam() {
        return null;
    }

    @Override
    public List<ICombatAction> getActions() {
        List<ICombatAction> actions = new ArrayList<>();

        Weapon w = EquipmentManager.GetInstance().getWeapon();
        if (w != null) {
            actions.addAll(w.getActions());
        }
        Accessory a = EquipmentManager.GetInstance().getAccessory();
        if (a instanceof IEquipment provider) {
            actions.addAll(provider.getActions());
        }
        return actions;
    }

    @Override public void setHP(int value) {
        this.hp = Math.min(value, maxHP);
    }
    @Override public void setMaxHP(int value) {
        this.maxHP = value;
    }
    @Override public void setAttack(int value) {
        this.attackDamage = Math.max(1, value);
    }
    @Override public void setAttackCount(int value) {
        this.attackCount = Math.max(1, value);
    }
    @Override public void setPenetration(int value) {
        this.attackPenetration = Math.max(0, value);
    }
    @Override public void setDefense(int value) {
        this.defense = Math.max(0, value);
    }
    @Override public void setSpeed(int value) {
        this.speed = value;
    }
    @Override public void setEvasion(int value) {
        this.evasion = value;
    }
    @Override public void setAccuracy(int value) {
        this.accuracy = value;
    }
    @Override public void setCritChance(int value) {
        this.critChance = value;
    }
    @Override public void setCritDamage(float value) {
        this.critDamage = value;
    }

    @Override
    public void setName(String value) {

    }

    @Override
    public void setTeam(Team value) {

    }

    @Override
    public void setActions(List<ICombatAction> value) {

    }

    @Override
    public void takeDamage(int amount) {
        int realDmg = Math.max(amount - defense, 0);
        hp -= realDmg;
    }

    // Player specific stuff
    public int getShakes() { return shakes; }
    public void setShakes(int shakes) { this.shakes = shakes; }

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public void addGold(int g) { this.gold += g; }

    public void AddHP(int amount) { this.hp = Math.min(maxHP, hp + amount); }

    public boolean hasAccessoryAction() { return false; }
    public void useAccessoryAction() {}
    public void setTempDefenseBoost(int i) {}

    public void addRandomConsumable() {}
    public void equipLegendaryWeapon() {}
    public void restoreFullHealth() { this.hp = maxHP; }
    public void boostAllStats(int i) {}

    public void increaseRandomStat() {
            Random random = new Random();
            int stat = random.nextInt(8);

            switch (stat) {
                case 0 -> setMaxHP(getMaxHP() + 1);
                case 1 -> setAttack(getAttack() + 1);
                case 2 -> setDefense(getDefense() + 1);
                case 3 -> setPenetration(getPenetration() + 1);
                case 4 -> setAttackCount(getAttackCount() + 1);
                case 5 -> setAccuracy(getAccuracy() + 1);
                case 6 -> setEvasion(getEvasion() + 1);
                case 7 -> setSpeed(getSpeed() + 1);
            }

    }
}