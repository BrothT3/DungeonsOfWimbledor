package UI;

import GameWorld.Player;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private final JLabel stats = new JLabel();

    public StatsPanel() {
        setBackground(Color.BLACK);
        stats.setFont(new Font("Monospaced", Font.PLAIN, 12));
        stats.setForeground(Color.GREEN);
        add(stats);
    }

    public void updateStats(Player p) {
        stats.setText(
            String.format("HP:%d/%d  ATK:%d  DEF:%d  GOLD:%d",
                p.getHP(), p.getMaxHP(),
                p.getAttack(), p.getDefense(),
                p.getGold()
            )
        );
    }
}
