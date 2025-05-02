package com.wimbledor;

import com.wimbledor.entities.Player;
import com.wimbledor.ui.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Application entry point. Restores the Main class to launch the MainFrame.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create or load your hero here
            Player player = new Player("Hero");
            // Launch the main window
            new MainFrame(player);
        });
    }
}