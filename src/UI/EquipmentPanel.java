package UI;

import GameWorld.EquipmentManager;
import GameWorld.Equipment.*;
import GameWorld.Player;

import javax.swing.*;
import java.awt.*;

public class EquipmentPanel extends JPanel {
    private final JLabel weapon    = new JLabel();
    private final JLabel armor     = new JLabel();
    private final JLabel accessory = new JLabel();

    public EquipmentPanel() {
        setLayout(new GridLayout(1,3,5,5));
        setBackground(new Color(20,20,20));
        for (JLabel l : new JLabel[]{weapon, armor, accessory}) {
            l.setOpaque(true);
            l.setBackground(new Color(50,50,50));
            l.setForeground(Color.CYAN);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            add(l);
        }
    }

    public void updateEquipment(Player p) {
        var em = EquipmentManager.GetInstance();
        Weapon    w = em.getWeapon();
        Armor     a = em.getArmor();
        Accessory x = em.getAccessory();

        weapon   .setText("Weapon: "   + (w!=null? w.getName():"None"));
        armor    .setText("Armor: "    + (a!=null? a.getName():"None"));
        accessory.setText("Accessory: "+ (x!=null? x.getName():"None"));

        weapon   .setToolTipText(w!=null? w.getDescription():"");
        armor    .setToolTipText(a!=null? a.getDescription():"");
        accessory.setToolTipText(x!=null? x.getDescription():"");
    }
}
