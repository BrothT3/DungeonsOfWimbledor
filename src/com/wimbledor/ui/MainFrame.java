package com.wimbledor.ui;

import com.wimbledor.entities.Player;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.engine.GameContext;
import com.wimbledor.equipment.EquipmentManager;
import com.wimbledor.equipment.weapons.CruddySword;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardView cardView;
    private final JSplitPane split;         // this is your field
    private final PlayerInfoPanel infoPanel;
    private final CardController controller;

    public MainFrame(Player player) {
        super("Dungeons of WimbleDor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left: the central card area
        cardView = new CardView();
        JScrollPane cardScroll = new JScrollPane(cardView);

        // Right: player stats / equipment / consumables
        infoPanel = new PlayerInfoPanel(player);

        // Build the encounter deck and controller
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());
        controller = new CardController(deck, cardView, this);

        // Wire up context
        GameContext.setPlayer(player);
        GameContext.setOnEncounterComplete(controller::drawNext);

        // Equip a starter weapon
        EquipmentManager.getInstance().equipWeapon(new CruddySword());

        // Now initialize your split field **once**, using the scroll pane on left
        split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                cardScroll,
                infoPanel
        );
        split.setResizeWeight(0.7);
        add(split, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Kick off the first card draw and stats refresh
        controller.start();
        infoPanel.refresh();
    }

    /** Swap out whatever is on the left (cardView or ActionPanel) */
    public void setCenterComponent(JComponent comp) {
        split.setLeftComponent(comp);
        split.revalidate();
        split.repaint();
    }

    /** Called after any change (narrative choice or battle) */
    public void refresh() {
        cardView.repaint();
        infoPanel.refresh();
        revalidate();
        repaint();
    }
}
