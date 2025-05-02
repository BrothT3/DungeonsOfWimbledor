package com.wimbledor.ui;

import javax.swing.*;
import java.awt.*;

public class SlotPanel extends JPanel {
    private final JLabel label = new JLabel("", SwingConstants.CENTER);

    public SlotPanel(String title) {
        setBorder(BorderFactory.createTitledBorder(title));
        setPreferredSize(new Dimension(80,80));
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
    }

    public void setContent(String text) {
        label.setText("<html><body style='text-align:center'>" + text + "</body></html>");
    }
}
