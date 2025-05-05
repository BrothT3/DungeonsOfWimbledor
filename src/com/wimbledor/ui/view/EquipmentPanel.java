package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Four equipment slots: weapon, armor, ring, accessory.
 */
public class EquipmentPanel extends JPanel {
    private final JLabel[] slots = new JLabel[4];

    public EquipmentPanel() {
        setLayout(new GridLayout(1,4,4,4));
        for (int i=0;i<4;i++) {
            slots[i] = new JLabel("[Empty]", SwingConstants.CENTER);
            slots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            add(slots[i]);
        }
    }

    public void updateEquipment(String weapon, String armor, String ring, String acc) {
        slots[0].setText(weapon);
        slots[1].setText(armor);
        slots[2].setText(ring);
        slots[3].setText(acc);
    }
}


// src/com/wimbledor/ui/ConsumablesPanel.java
