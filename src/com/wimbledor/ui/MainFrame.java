package com.wimbledor.ui;

import com.wimbledor.entities.Player;
import com.wimbledor.engine.EncounterDeck;
import com.wimbledor.engine.EncounterFactory;
import com.wimbledor.engine.GameContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardView cardView;
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

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                cardScroll,
                infoPanel
        );
        split.setResizeWeight(0.7);
        add(split, BorderLayout.CENTER);

        // Build the encounter deck and controller
        EncounterDeck deck = new EncounterDeck(EncounterFactory.generateEncounters());
        controller = new CardController(deck, cardView, this);

        // Wire up context
        GameContext.setPlayer(player);
        GameContext.setOnEncounterComplete(controller::drawNext);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        controller.start();
        infoPanel.refresh();
    }

    /** Called after any change (narrative choice or battle) */
    public void refresh() {
        infoPanel.refresh();
    }
}
