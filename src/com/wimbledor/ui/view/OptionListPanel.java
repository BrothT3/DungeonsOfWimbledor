// src/com/wimbledor/ui/view/OptionListPanel.java
package com.wimbledor.ui.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Renders a vertical list of textual options, each with a hotkey prefix.
 * Supports mouse-click and numeric hotkey selection.
 */
public class OptionListPanel extends JPanel {
    private Consumer<String> clickListener;

    public OptionListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                String code = String.valueOf(Character.toUpperCase(c));
                if (clickListener != null) {
                    clickListener.accept(code);
                }
            }
        });
    }

    /**
     * Populate the panel with option strings like "A. Attack".
     */
    public void setOptions(List<String> opts) {
        removeAll();
        for (String opt : opts) {
            JLabel label = new JLabel(opt);
            label.setForeground(Color.YELLOW);
            label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (clickListener != null) {
                        // extract code before the dot, e.g. "A"
                        String code = opt.split("\\.")[0].trim();
                        clickListener.accept(code);
                    }
                }
            });
            add(label);
        }
        revalidate();
        repaint();
        requestFocusInWindow();
    }

    /** Set the callback to receive the option's hotkey code. */
    public void setOptionClickListener(Consumer<String> listener) {
        this.clickListener = listener;
    }
}
