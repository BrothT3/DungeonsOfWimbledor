package com.wimbledor.ui;

import com.wimbledor.combat.ICombatAction;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ActionPanel extends JPanel {
    private Consumer<ICombatAction> onActionClicked;

    public ActionPanel() {
        setLayout(new BorderLayout(5,5));
        setBackground(new Color(0xFFEB3B));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Actions",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                getFont().deriveFont(Font.BOLD, 14f)
        ));
    }

    /** Controller calls this once, to install its callback. */
    public void setActionClickListener(Consumer<ICombatAction> listener) {
        this.onActionClicked = listener;
    }

    /**
     * Pure‐UI rebuild of all buttons.
     * Clicking a button simply fires onActionClicked.accept(act).
     */
    public void updateActions(List<ICombatAction> actions) {
        removeAll();

        // 1) Header
        JLabel header = new JLabel("Choose an action:", SwingConstants.CENTER);
        header.setFont(getFont().deriveFont(Font.BOLD, 12f));
        add(header, BorderLayout.NORTH);

        // 2) Button list
        JPanel btnContainer = new JPanel(new GridLayout(0, 1, 5, 5));
        btnContainer.setBackground(new Color(0xFFEB3B));
        for (ICombatAction act : actions) {
            JButton btn = new JButton(act.getName());
            btn.addActionListener(e -> {
                if (onActionClicked != null) {
                    onActionClicked.accept(act);
                }
            });
            btnContainer.add(btn);
        }

        // 3) Scroll pane
        JScrollPane scroll = new JScrollPane(
                btnContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(0xFFEB3B));
        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}