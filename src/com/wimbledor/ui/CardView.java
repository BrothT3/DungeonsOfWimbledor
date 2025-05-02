package com.wimbledor.ui;

import com.wimbledor.assets.ICard;
import com.wimbledor.assets.CardOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CardView extends JPanel {
    private final JLabel titleLabel = new JLabel();
    private final JTextArea descArea = new JTextArea(5, 30);
    private final JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

    public CardView() {
        setLayout(new BorderLayout(10, 10));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(descArea), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void display(ICard card, ActionListener listener) {
        titleLabel.setText(card.getTitle());
        descArea.setText(card.getDescription());

        buttonsPanel.removeAll();
        for (CardOption opt : card.getOptions()) {
            JButton btn = new JButton(opt.getLabel());
            btn.setActionCommand(opt.getCode());
            btn.addActionListener(listener);
            buttonsPanel.add(btn);
        }
        revalidate();
        repaint();
    }
}
