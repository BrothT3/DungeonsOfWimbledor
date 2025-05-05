package com.wimbledor.ui.view;

import com.wimbledor.combat.CombatActions.ICombatAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A row of buttons representing the player's available combat actions.
 */
public class ActionListPanel extends JPanel {

    public interface ActionClickListener {
        void onActionClicked(ICombatAction action);
    }

    private ActionClickListener listener;

    public ActionListPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
        setBackground(Color.BLACK);
    }

    /**
     * Set the available actions; rebuilds buttons accordingly.
     */
    public void setActions(List<ICombatAction> actionsList) {
        removeAll();

        if (actionsList == null) return;

        for (ICombatAction act : actionsList) {
            JButton btn = new JButton(act.getName());
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.YELLOW);
            btn.setFont(new Font("Monospaced", Font.BOLD, 12));
            btn.setToolTipText(act.getDescription()); // optional detail

            btn.addActionListener(e -> {
                if (listener != null) listener.onActionClicked(act);
            });

            add(btn);
        }

        revalidate();
        repaint();
    }

    /**
     * Register a callback for when buttons are clicked.
     */
    public void setActionClickListener(ActionClickListener listener) {
        this.listener = listener;
    }
}
