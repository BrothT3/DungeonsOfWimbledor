package com.wimbledor.ui.view;

import com.wimbledor.assets.CardOption;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Displays a narrative encounter stage: title, description, options, and info log.
 */
public class NarrativePanel extends JPanel {
    private final JLabel titleLabel;
    private final JTextArea descriptionArea;
    private final OptionListPanel optionsPanel;
    private final JTextArea infoArea;

    public NarrativePanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.BLACK);

        // Title at top
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Center: description and options
        JPanel center = new JPanel(new BorderLayout(4, 4));
        center.setBackground(Color.BLACK);

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setBackground(Color.DARK_GRAY);
        descriptionArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(BorderFactory.createTitledBorder(""));
        center.add(descScroll, BorderLayout.CENTER);

        optionsPanel = new OptionListPanel();
        optionsPanel.setBackground(Color.BLACK);
        center.add(optionsPanel, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        // Info at bottom
        infoArea = new JTextArea(3, 0);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setEditable(false);
        infoArea.setForeground(Color.CYAN);
        infoArea.setBackground(Color.BLACK);
        infoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(infoArea), BorderLayout.SOUTH);
    }

    /**
     * Shows a new stage: updates title, description, and options list.
     * Accepts a list of CardOption, which includes labels and codes.
     */
    public void showStage(String title, String description, List<CardOption> options) {
        titleLabel.setText(title);
        descriptionArea.setText(description);

        // Convert to string labels for display
        List<String> optionLabels = options.stream()
                .map(opt -> opt.getCode() + ". " + opt.getLabel())
                .toList();

        optionsPanel.setOptions(optionLabels);
        clearInfo();
    }

    public void appendInfo(String text) {
        infoArea.append(text + "\n");
        infoArea.setCaretPosition(infoArea.getDocument().getLength());
    }

    public void clearInfo() {
        infoArea.setText("");
    }

    public void setOptionClickListener(java.util.function.Consumer<String> listener) {
        optionsPanel.setOptionClickListener(listener);
    }
}
