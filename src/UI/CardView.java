package UI;

import Cards.ICard;
import Cards.CardOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CardView extends JPanel {
    private final JLabel titleLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea();
    private final JPanel optionsPanel = new JPanel();

    public CardView() {
        setLayout(new BorderLayout(10,10));
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.SOUTH);
    }

    /**
     * Display a card and wire each button back to the given listener.
     * @param card the card to show
     * @param listener will receive an ActionEvent whose actionCommand = option.getCode()
     */
    public void display(ICard card, ActionListener listener) {
        titleLabel.setText(card.getTitle());
        descriptionArea.setText(card.getDescription());

        optionsPanel.removeAll();
        optionsPanel.setLayout(new GridLayout(0, 1, 5, 5));
        for (CardOption opt : card.getOptions()) {
            JButton btn = new JButton(opt.getLabel());
            btn.setActionCommand(opt.getCode());
            btn.addActionListener(listener);
            optionsPanel.add(btn);
        }
        revalidate();
        repaint();
    }
}