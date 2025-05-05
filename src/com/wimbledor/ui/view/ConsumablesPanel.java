package com.wimbledor.ui.view;

import com.wimbledor.combat.CombatActions.ICombatAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Displays up to three consumable slots.
 */
public class ConsumablesPanel extends JPanel {
    private final JLabel[] slots = new JLabel[3];

    public ConsumablesPanel() {
        setLayout(new GridLayout(1, 3, 4, 4));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Consumables"));

        for (int i = 0; i < 3; i++) {
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
     * Sets the visible consumables.
     */
    public void setConsumables(List<ICombatAction> consumables) {
        for (int i = 0; i < 3; i++) {
            if (i < consumables.size()) {
                ICombatAction action = consumables.get(i);
                slots[i].setText(action.getName());
                slots[i].setToolTipText(action.getDescription());
            } else {
                slots[i].setText("[Empty]");
                slots[i].setToolTipText(null);
            }
        }
    }
}
