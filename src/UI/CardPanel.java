package UI;

import Cards.*;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;
import GameWorld.Enums.TargetMode;
import GameWorld.Player;
import GameWorld.TurnManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CardPanel extends JPanel {
    private final JLabel    titleLabel         = new JLabel();
    private final JTextArea descriptionArea    = new JTextArea();
    private final JPanel    actionButtonsPanel = new JPanel(new FlowLayout());
    private final JPanel    targetButtonsPanel = new JPanel(new FlowLayout());
    private final JTextArea logArea            = new JTextArea(5,30);
    private final JScrollPane logScrollPane;
    private final Color     defaultTitleColor  = Color.WHITE;
    private Timer           flashTimer;

    public CardPanel() {
        super(new BorderLayout());
        setBackground(new Color(30,30,30));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Title
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(defaultTitleColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Description
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setForeground(Color.LIGHT_GRAY);
        descriptionArea.setOpaque(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        add(descriptionArea, BorderLayout.CENTER);

        // Action buttons (bottom)
        actionButtonsPanel.setOpaque(false);
        add(actionButtonsPanel, BorderLayout.SOUTH);

        // Target buttons (left of actions)
        targetButtonsPanel.setOpaque(false);
        add(targetButtonsPanel, BorderLayout.WEST);

        // Combat log (right)
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

    /**
     * Refresh this panel for the given card & player.
     */
    public void updateCard(BaseCard card, Player player) {
        // 1) Title & description
        titleLabel.setText(card.getTitle());
        descriptionArea.setText(card.getText());

        // 2) Clear old buttons
        actionButtonsPanel.removeAll();
        targetButtonsPanel.removeAll();

        // 3a) EncounterCard → show its options
        if (card instanceof EncounterCard ec) {
            for (StageOption opt : ec.getCurrentStage().getOptions()) {
                addOptionButton(opt.getCode(), opt.getLabel());
            }
        }
        // 3b) BattleCard → *nothing here* (buttons come from startBattle() or updateUI())
        else if (card instanceof BattleCard) {
            // Intentionally blank
        }
        // 3c) EventCard → legacy swipe
        else if (card instanceof EventCard) {
            addSwipeButton("← Left",  "LEFT");
            addSwipeButton("↑ Up",    "UP");
            addSwipeButton("→ Right", "RIGHT");
        }

        revalidate();
        repaint();
    }

    // ─── Combat ─────────────────────────────────────────────────────────────

    void showActions(ICombatEntity actor) {
        actionButtonsPanel.removeAll();
        targetButtonsPanel.removeAll();

        for (ICombatAction act : actor.getActions()) {
            JButton b = new JButton(act.getLabel());
            b.addActionListener(e -> onCombatActionSelected(actor, act));
            actionButtonsPanel.add(b);
        }

        revalidate();
        repaint();
    }

    private void onCombatActionSelected(ICombatEntity actor, ICombatAction action) {
        TurnManager tm = GameFrame.getInstance().getTurnManager();
        List<ICombatEntity> targets;

        switch (action.getTargetMode()) {
            case SELF -> {
                performCombat(actor, List.of(actor), action);
                return;
            }
            case ALL_ENEMIES -> {
                targets = tm.getEnemiesOf(actor.getTeam());
                performCombat(actor, targets, action);
                return;
            }
            case ALL_ALLIES -> {
                targets = tm.getEntitiesOnTeam(actor.getTeam());
                performCombat(actor, targets, action);
                return;
            }
            case ALL_ENTITIES -> {
                targets = tm.getAllEntities();
                performCombat(actor, targets, action);
                return;
            }
            case SINGLE_ENEMY -> {
                targets = tm.getEnemiesOf(actor.getTeam());
                if (targets.size() == 1) {
                    performCombat(actor, targets, action);
                    return;
                }
                targetButtonsPanel.removeAll();
                for (ICombatEntity t : targets) {
                    JButton tb = new JButton(t.getName());
                    tb.addActionListener(evt -> performCombat(actor, List.of(t), action));
                    targetButtonsPanel.add(tb);
                }
                revalidate();
                repaint();
                return;
            }
        }
    }

    private void performCombat(ICombatEntity actor,
                               List<ICombatEntity> targets,
                               ICombatAction action)
    {
        action.execute(actor, targets);
        TurnManager tm = GameFrame.getInstance().getTurnManager();
        tm.startNextTurn();

        if (tm.isBattleOver()) {
            GameFrame.getInstance().onBattleComplete();
            return;
        }

        ICombatEntity next = tm.getCurrentEntity();
        if (next instanceof Player) {
            showActions(next);
        } else {
            // AI turn
            ICombatAction aiAct = next.getActions().get(0);
            List<ICombatEntity> aiTargets = tm.getEnemiesOf(next.getTeam());
            aiAct.execute(next, aiTargets);
            tm.startNextTurn();
            showActions(tm.getCurrentEntity());
        }

        GameFrame.getInstance().updateUI();
    }

    // ─── Encounter & Event ──────────────────────────────────────────────────

    private void addOptionButton(String code, String label) {
        JButton b = new JButton(label);
        b.setActionCommand(code);
        b.addActionListener(e -> GameFrame.getInstance().processAction(code));
        actionButtonsPanel.add(b);
    }

    private void addSwipeButton(String text, String cmd) {
        JButton b = new JButton(text);
        b.addActionListener(e -> GameFrame.getInstance().processAction(cmd));
        actionButtonsPanel.add(b);
    }

    // ─── Log & Flash ───────────────────────────────────────────────────────

    public void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void flash(Color c) {
        titleLabel.setForeground(c);
        if (flashTimer != null && flashTimer.isRunning()) flashTimer.stop();
        flashTimer = new Timer(250, e -> {
            titleLabel.setForeground(defaultTitleColor);
            flashTimer.stop();
        });
        flashTimer.setRepeats(false);
        flashTimer.start();
    }
}
