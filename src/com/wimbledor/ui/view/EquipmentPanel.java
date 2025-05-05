package com.wimbledor.ui.view;

import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.IEquipment;

import javax.swing.*;
import java.awt.*;

/**
 * Four equipment slots: weapon, armor, accessory1, accessory2 (future).
 * Future 4th slot might represent offhand/shield or secondary item.
 */
public class EquipmentPanel extends JPanel {
    private final JLabel[] slots = new JLabel[4];

    public EquipmentPanel() {
        setLayout(new GridLayout(1, 4, 4, 4));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Equipment"));

        for (int i = 0; i < 4; i++) {
            slots[i] = new JLabel("[Empty]", SwingConstants.CENTER);
            slots[i].setForeground(Color.WHITE);
            slots[i].setBackground(Color.DARK_GRAY);
            slots[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            slots[i].setOpaque(true);
            slots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            add(slots[i]);
        }
    }

    /**
     * Update all equipment slots using the singleton EquipmentManager.
     */
    public void setEquipment(EquipmentManager manager) {
        IEquipment weapon = manager.getEquippedWeapon();
        IEquipment armor = manager.getEquippedArmor();
        IEquipment accessory = manager.getEquippedAccessory();

        updateEquipment(
                weapon != null ? weapon.getName() : "[Weapon]",
                armor != null ? armor.getName() : "[Armor]",
                accessory != null ? accessory.getName() : "[Acc 1]",
                "[Acc 2]" // Reserved for second accessory or offhand
        );
    }

    private void updateEquipment(String weapon, String armor, String acc1, String acc2) {
        slots[0].setText(weapon);
        slots[1].setText(armor);
        slots[2].setText(acc1);
        slots[3].setText(acc2);
    }
}
