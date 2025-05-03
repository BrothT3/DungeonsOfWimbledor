// src/com/wimbledor/ui/MainFrame.java
package com.wimbledor.ui;

import com.wimbledor.combat.TurnManager;
import com.wimbledor.engine.GameContext;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.combat.ICombatAction;
import com.wimbledor.entities.Player;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.weapons.CruddySword;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class MainFrame extends JFrame {
    private final CardView       cardView;
    private final EnemyPanel     enemyPanel;
    private final ActionPanel    actionPanel;
    private final LogPanel       logPanel;
    private final PlayerInfoPanel infoPanel;
    private final JPanel         leftContainer;
    private final JSplitPane     split;
    private final CardController controller;

    public MainFrame(Player player) {
        super("Dungeons of WimbleDor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // right‐side status panel
        infoPanel = new PlayerInfoPanel(player);

        // left stack: narrative or combat UI
        leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));
        cardView    = new CardView();
        enemyPanel  = new EnemyPanel();
        actionPanel = new ActionPanel();
        logPanel    = new LogPanel();

        // start with narrative view
        enemyPanel.setVisible(false);
        actionPanel.setVisible(false);
        logPanel.setVisible(false);

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

        // set up player, gear, encounter deck
        GameContext.setPlayer(player);
        EquipmentManager.getInstance().equipWeapon(new CruddySword());
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());

        // instantiate controller and let it wire everything
        controller = new CardController(
                deck, cardView, enemyPanel, actionPanel, logPanel, this
        );
        // game‐wide logger → logPanel
        GameContext.setLogger(logPanel::append);

        controller.start();  // kicks off the first draw

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Called by CardController to swap into combat mode.
     * Pass in the live enemies, your TurnManager, and the controller’s action‐handler.
     */
    public void refreshCombatUI(
            List<ICombatEntity> enemies,
            TurnManager tm,
            BiConsumer<ICombatAction, List<ICombatEntity>> onActionSelected
    ) {
        // show the panels
        cardView   .setVisible(false);
        enemyPanel .setVisible(true);
        actionPanel.setVisible(true);
        logPanel   .setVisible(true);

        // drive the data
        enemyPanel .updateEnemies(enemies);
        actionPanel.updateActions(
                tm.getPlayerEntity().getAvailableActions()
        );

        // leave the wiring of click‐listeners to your controller:
        //   controller.initCombatListeners(tm, onActionSelected);
        // You could also expose these panels via getters (below) so
        // CardController can say:
        //    getActionPanel().setActionClickListener(...)
        //    getEnemyPanel().setEnemyClickListener(...)
    }

    /** Switch back to narrative mode */
    public void refreshNarrativeUI() {
        cardView   .setVisible(true);
        enemyPanel .setVisible(false);
        actionPanel.setVisible(false);
        logPanel   .setVisible(false);
        cardView.repaint();
    }

    // ——— Helpers for your controller wiring ———

    /** Let CardController hook into user clicks on actions. */
    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    /** Let CardController hook into user clicks on enemies. */
    public EnemyPanel getEnemyPanel() {
        return enemyPanel;
    }

    /** Expose the log so CardController can append messages directly. */
    public LogPanel getLogPanel() {
        return logPanel;
    }

    /** Always repaint everything. */
    public void refreshAll() {
        leftContainer.revalidate();
        leftContainer.repaint();
        infoPanel.refresh();
        repaint();
    }
}
