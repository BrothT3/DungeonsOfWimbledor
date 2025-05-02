package com.wimbledor.ui;

import com.wimbledor.entities.Player;
import com.wimbledor.equipment.IEquipment;
import com.wimbledor.equipment.EquipmentManager;

import javax.swing.*;
import java.awt.*;

public class EquipmentPanel extends JPanel {
    private final Player player;
    private final SlotPanel weaponSlot;
    private final SlotPanel armorSlot;
    private final SlotPanel accessorySlot;

    public EquipmentPanel(Player player) {
        this.player = player;
        setBorder(BorderFactory.createTitledBorder("Equipment"));
        setLayout(new GridLayout(1,3,8,8));

        weaponSlot    = new SlotPanel("Weapon");
        armorSlot     = new SlotPanel("Armor");
        accessorySlot = new SlotPanel("Accessory");

        add(weaponSlot);
        add(armorSlot);
        add(accessorySlot);

        refresh();
    }

    public void refresh() {
        EquipmentManager em = EquipmentManager.getInstance();

        IEquipment w = em.getEquippedWeapon();
        IEquipment a = em.getEquippedArmor();
        IEquipment ac= em.getEquippedAccessory();

        weaponSlot.setContent(w != null ? w.getName() : "(none)");
        armorSlot.setContent(a != null ? a.getName() : "(none)");
        accessorySlot.setContent(ac!= null ? ac.getName(): "(none)");
    }
}
