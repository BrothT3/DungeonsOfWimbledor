package UI;

import Cards.BaseCard;
import Cards.EventCard;
import Cards.MonsterCard;
import GameWorld.CombatUtils;
import GameWorld.Enums.Team;
import GameWorld.Enums.TargetMode;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;
import GameWorld.Player;
import GameWorld.TurnManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CardPanel extends JPanel {
    private final JLabel    titleLabel           = new JLabel();
    private final JTextArea descriptionArea      = new JTextArea();
    private final JPanel    actionButtonsPanel   = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final JPanel    targetButtonsPanel   = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final JTextArea logArea              = new JTextArea(5,30);
    private final JScrollPane logScrollPane;
    private final Color     defaultBackground;
    private final Color     defaultTitleColor    = Color.WHITE;
    private Timer           flashTimer;

    public CardPanel() {
        super(new BorderLayout());
        defaultBackground = new Color(30,30,30);
        setBackground(defaultBackground);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // ─── Title ─────────────────────────────────────────────────────────────
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(defaultTitleColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // ─── Description ─────────────────────────────────────────────────────
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setForeground(Color.LIGHT_GRAY);
        descriptionArea.setOpaque(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        add(descriptionArea, BorderLayout.CENTER);

        // ─── Action Buttons ───────────────────────────────────────────────────
        actionButtonsPanel.setOpaque(false);
        add(actionButtonsPanel, BorderLayout.SOUTH);

        // ─── Target Buttons ───────────────────────────────────────────────────
        targetButtonsPanel.setOpaque(false);
        add(targetButtonsPanel, BorderLayout.WEST);

        // ─── Combat Log ───────────────────────────────────────────────────────
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN,12));
        logArea.setForeground(Color.WHITE);
        logArea.setBackground(Color.BLACK);
        logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(250,100));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Combat Log"));
        add(logScrollPane, BorderLayout.EAST);
    }


    public void updateCard(BaseCard card) {
        titleLabel.setText(card.getTitle());
        descriptionArea.setText(card.getText());

        actionButtonsPanel.removeAll();
        targetButtonsPanel.removeAll();

        TurnManager tm = GameFrame.getInstance().getTurnManager();

        if (card instanceof MonsterCard && tm != null) {
            ICombatEntity current = tm.getCurrentEntity();
            if (current instanceof Player) {
                showActions(current);
            }
        } //skal ændres til noget tilsvarende eventkortet
        else if (card instanceof EventCard) {
            addSwipeButton("Left",  "LEFT");
            addSwipeButton("Up",    "UP");
            addSwipeButton("Right", "RIGHT");
        }

        revalidate();
        repaint();
    }

    public void showActions(ICombatEntity actor) {
        actionButtonsPanel.removeAll();
        targetButtonsPanel.removeAll();

        for (ICombatAction action : actor.getActions()) {
            JButton b = new JButton(action.getLabel());
            b.addActionListener(e -> onActionSelected(actor, action));
            actionButtonsPanel.add(b);
        }

        revalidate();
        repaint();
    }

    private void onActionSelected(ICombatEntity actor, ICombatAction action) {
        TargetMode mode = action.getTargetMode();
        List<ICombatEntity> enemies;
        TurnManager tm = GameFrame.getInstance().getTurnManager();
        Team team = actor.getTeam();

        switch (mode) {
            case SELF -> {
                performAction(actor, List.of(actor), action);
                return;
            }
            case ALL_ENEMIES -> {
                enemies = tm.getEnemiesOf(team);
                performAction(actor, enemies, action);
                return;
            }
            case ALL_ALLIES -> {
                enemies = tm.getEntitiesOnTeam(team);
                performAction(actor, enemies, action);
                return;
            }
            case ALL_ENTITIES -> {
                enemies = tm.getAllEntities();
                performAction(actor, enemies, action);
                return;
            }
            case SINGLE_ENEMY -> {
                enemies = tm.getEnemiesOf(team);
                if (enemies.size() == 1){
                    performAction(actor, enemies, action);
                    return;
                }
                targetButtonsPanel.removeAll();
                for (ICombatEntity tgt : enemies) {
                    JButton tb = new JButton(tgt.getName());
                    tb.addActionListener(evt -> performAction(actor, List.of(tgt), action));
                    targetButtonsPanel.add(tb);
                }
                revalidate();
                repaint();
                return;
            }
            default -> {}
        }
    }

    private void performAction(ICombatEntity actor,
                               List<ICombatEntity> targets,
                               ICombatAction action) {
        action.execute(actor, targets);

        TurnManager tm = GameFrame.getInstance().getTurnManager();
        tm.startNextTurn();

        if (tm.isBattleOver()) {
            GameFrame.getInstance().handleBattleEnd();
            return;
        }

        ICombatEntity next = tm.getCurrentEntity();
        if (next instanceof Player) {
            showActions(next);
        }
        else {
            ICombatAction aiAct = next.getActions().get(0);
            List<ICombatEntity> aiTargs = tm.getEnemiesOf(next.getTeam());
            aiAct.execute(next, aiTargs);

            tm.startNextTurn();
            showActions(tm.getCurrentEntity());
        }

        GameFrame.getInstance().updateUI();
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void flash(Color color) {
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.stop();
        }
        titleLabel.setForeground(color);

        flashTimer = new Timer(250, e -> {
            titleLabel.setForeground(defaultTitleColor);
            flashTimer.stop();
        });
        flashTimer.setRepeats(false);
        flashTimer.start();
    }

    private void addSwipeButton(String text, String cmd) {
        JButton b = new JButton(text);
        b.addActionListener(e -> GameFrame.getInstance().processAction(cmd));
        actionButtonsPanel.add(b);
    }
}
