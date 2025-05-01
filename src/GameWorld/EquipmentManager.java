package GameWorld;

import GameWorld.Equipment.*;
import java.util.*;

public class EquipmentManager {
    private static EquipmentManager INSTANCE;

    private Weapon weapon;
    private Armor armor;
    private Accessory accessory;
    private final List<Consumable> consumables = new ArrayList<>();

    private EquipmentManager() {}

    public static EquipmentManager GetInstance() {
        if (INSTANCE == null) INSTANCE = new EquipmentManager();
        return INSTANCE;
    }

    public void equipWeapon(Weapon w, Player player) {
        this.weapon = w;
        w.applyToPlayer(player);
    }

    public void equipArmor(Armor a, Player player) {
        this.armor = a;
        a.applyToPlayer(player);
    }

    public void equipAccessory(Accessory a, Player player) {
        this.accessory = a;
        a.applyToPlayer(player);
    }

    public void addConsumable(Consumable c) {
        if (consumables.size() < 3) {
            consumables.add(c);
        }
    }

    public void useConsumable(int index, Player player) {
        if (index >= 0 && index < consumables.size()) {
            consumables.get(index).applyEffect(player);
            consumables.remove(index);
        }
    }

    public Weapon getWeapon() { return weapon; }
    public Armor getArmor() { return armor; }
    public Accessory getAccessory() { return accessory; }
    public List<Consumable> getConsumables() { return consumables; }
}