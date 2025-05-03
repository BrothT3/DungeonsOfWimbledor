// src/com/wimbledor/ui/MainFrame.java
package com.wimbledor.ui;

import com.wimbledor.combat.TurnManager;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.Player;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.weapons.CruddySword;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final CardView       cardView;

    private final EnemyPanel     enemyPanel;
    private  ActionPanel    actionPanel;
    private final LogPanel       logPanel;
    private final PlayerInfoPanel infoPanel;
    private final JPanel         leftContainer;
    private final JSplitPane     split;
    private final CardController controller;

    public MainFrame(Player player) {
        super("Dungeons of WimbleDor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // right stats
        infoPanel = new PlayerInfoPanel(player);

        // left vertical stack
        leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        cardView    = new CardView();
        enemyPanel  = new EnemyPanel(List.of());
        actionPanel = new ActionPanel();
        logPanel    = new LogPanel();

        enemyPanel .setVisible(false);
        actionPanel.setVisible(false);
        logPanel   .setVisible(false);

        leftContainer.add(cardView);
        leftContainer.add(enemyPanel);
        leftContainer.add(actionPanel);
        leftContainer.add(logPanel);

        split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(leftContainer),
                infoPanel
        );
        split.setResizeWeight(0.7);
        add(split, BorderLayout.CENTER);
        GameContext.setPlayer(player);
        EquipmentManager.getInstance().equipWeapon(new CruddySword());
        // controller wiring
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());
        controller = new CardController(
                deck, cardView, enemyPanel, actionPanel, logPanel, this
        );
        GameContext.setLogger(logPanel::append);
        // start
        controller.start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void refreshCombatUI(
            java.util.List<com.wimbledor.entities.ICombatEntity> enemies,
            TurnManager tm,
            java.util.function.BiConsumer<com.wimbledor.combat.ICombatAction,
                    java.util.List<com.wimbledor.entities.ICombatEntity>> onActionSelected
    ) {
        leftContainer.removeAll();

        // 1) Enemy row
        enemyPanel.updateEnemies(enemies);
        leftContainer.add(enemyPanel);

        // 2) Action row
        actionPanel = new ActionPanel();
        actionPanel.updateActions(tm, onActionSelected);
        leftContainer.add(actionPanel);

        // 3) Log row
        leftContainer.add(logPanel);

        leftContainer.revalidate();
        leftContainer.repaint();
    }
    /**
     * Lets the controller swap *exactly* what sits on the left of the split.
     * Internally we wrap in a scroll‐pane if you pass a raw component.
     */
    public void setCenterComponent(JComponent comp) {
        Component left;
        if (comp instanceof JScrollPane) {
            left = comp;
        } else {
            left = new JScrollPane(comp);
        }
        split.setLeftComponent(left);
        split.setDividerLocation(0.7);
        revalidate();
        repaint();
    }

    /** Refresh all panels. */
    public void refresh() {
        leftContainer.revalidate();
        leftContainer.repaint();
        infoPanel.refresh();
        revalidate();
        repaint();
    }
}
