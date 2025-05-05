package com.wimbledor;

import com.wimbledor.entities.Player;
import com.wimbledor.ui.controller.GameController;

import javax.swing.SwingUtilities;

/**
 * Application entry point. Launches the GameController on the Swing EDT.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create or load your hero here
            Player player = new Player("Hero");
            // Launch the top‐level GameController (it will create MainFrame)
            GameController.launch(player);
        });
    }
}