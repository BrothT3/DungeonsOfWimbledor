package UI;

import GameWorld.EquipmentManager;
import GameWorld.Equipment.Consumable;
import GameWorld.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsumablesPanel extends JPanel {
    private final JButton[] slots = new JButton[3];

    public ConsumablesPanel() {
        setLayout(new GridLayout(1,3,5,5));
        setBackground(new Color(20,20,20));
        for (int i = 0; i < 3; i++) {
            slots[i] = new JButton("Empty");
            slots[i].setEnabled(false);
            slots[i].setBackground(new Color(40,40,40));
            slots[i].setForeground(Color.ORANGE);
            add(slots[i]);
        }
    }

    public void updateConsumables(Player p) {
        List<Consumable> list = EquipmentManager.GetInstance().getConsumables();
        for (int i = 0; i < 3; i++) {
            if (i < list.size()) {
                Consumable c = list.get(i);
                slots[i].setText(c.getName());
                slots[i].setToolTipText(c.getDescription());
                slots[i].setEnabled(true);
                int idx = i;
                slots[i].addActionListener(e -> EquipmentManager.GetInstance().useConsumable(idx, p));
            } else {
                slots[i].setText("Empty");
                slots[i].setToolTipText("");
                slots[i].setEnabled(false);
                for (var l : slots[i].getActionListeners()) {
                    slots[i].removeActionListener(l);
                }
            }
        }
    }
}
