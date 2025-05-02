package UI;

import Cards.*;
import GameWorld.*;
import GameWorld.Interfaces.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CardDisplayPanel extends JPanel {
    private final JLabel title    = new JLabel("", SwingConstants.CENTER);
    private final JTextArea body  = new JTextArea();

    public CardDisplayPanel() {
        setLayout(new BorderLayout(5,5));
        setBackground(new Color(30,30,30));
        setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        title.setFont(new Font("Serif",Font.BOLD,24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        body.setFont(new Font("Monospaced",Font.PLAIN,14));
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);
        add(new JScrollPane(body), BorderLayout.CENTER);
    }

    public void updateCard(BaseCard card, Player player, TurnManager tm) {

        title.setText(card.getTitle());
        StringBuilder txt = new StringBuilder(card.getText()).append("\n\n");
        if (card instanceof MonsterCard || tm != null) {
            List<ICombatEntity> monsters = tm==null
                ? List.of((ICombatEntity)card)
                : tm.getEnemiesOf(player.getTeam());
            for (ICombatEntity m : monsters) {
                txt.append(m.getName()).append(" (HP:").append(m.getHP()).append(")\n");
            }
        }
        body.setText(txt.toString());
    }
}
