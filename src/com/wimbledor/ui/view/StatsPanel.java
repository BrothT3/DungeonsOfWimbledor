package com.wimbledor.ui.view;

import com.wimbledor.combat.enums.DerivedStat;
import com.wimbledor.combat.enums.Stat;
import com.wimbledor.entities.ICombatEntity;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the player's HP and six core stats.
 */
public class StatsPanel extends JPanel {

    private final JLabel hpLabel = new JLabel("HP: ???");
    private final JLabel strength = new JLabel();
    private final JLabel agility = new JLabel();
    private final JLabel endurance = new JLabel();
    private final JLabel willpower = new JLabel();
    private final JLabel knowledge = new JLabel();
    private final JLabel cunning = new JLabel();

    public StatsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder("Stats"));

        Font statFont = new Font("Monospaced", Font.PLAIN, 12);
        Color statColor = Color.WHITE;

        for (JLabel label : new JLabel[] {
                hpLabel, strength, agility, endurance, willpower, knowledge, cunning
        }) {
            label.setForeground(statColor);
            label.setFont(statFont);
            add(label);
        }
    }

    /**
     * Updates stat values from the current player.
     */
    public void setStats(ICombatEntity player) {
        // You can swap out "???" for player.getCurrentHP() / getMaxHP() if those exist
        hpLabel.setText("HP: " + player.getCurrentHp() + "/" + player.getDerived(DerivedStat.MAX_HP));

        strength.setText("STR: " + player.getStat(Stat.STRENGTH));
        agility.setText("AGI: " + player.getStat(Stat.AGILITY));
        endurance.setText("END: " + player.getStat(Stat.ENDURANCE));
        willpower.setText("WIL: " + player.getStat(Stat.WILLPOWER));
        knowledge.setText("KNO: " + player.getStat(Stat.KNOWLEDGE));
        cunning.setText("CUN: " + player.getStat(Stat.CUNNING));
    }
}
