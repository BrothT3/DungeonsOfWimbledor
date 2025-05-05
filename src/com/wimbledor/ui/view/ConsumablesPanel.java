package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Exactly three consumable slots.
 */
public class ConsumablesPanel extends JPanel {
    private final JLabel[] slots = new JLabel[3];

    public ConsumablesPanel() {
        setLayout(new GridLayout(1,3,4,4));
        for (int i=0;i<3;i++) {
            slots[i] = new JLabel("[ ]", SwingConstants.CENTER);
            slots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            add(slots[i]);
        }
    }
    public void updateConsumables(String c1, String c2, String c3) {
        slots[0].setText(c1);
        slots[1].setText(c2);
        slots[2].setText(c3);
    }
}