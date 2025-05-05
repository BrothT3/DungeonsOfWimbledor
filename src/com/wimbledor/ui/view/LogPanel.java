// src/com/wimbledor/ui/LogPanel.java
package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Black‐background scrolling text log for combat events.
 */
public class LogPanel extends JPanel {
    private final JTextArea logArea = new JTextArea(5, 30);

    public LogPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(
                logArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        setBorder(BorderFactory.createTitledBorder("Combat Log"));
        scroll.getViewport().setBackground(Color.BLACK);

        add(scroll, BorderLayout.CENTER);
    }

    /** Append a line and auto‐scroll to bottom. */
    public void append(String line) {
        logArea.append(line + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    /** Clear the log. */
    public void clear() {
        logArea.setText("");
    }
}
