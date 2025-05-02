package UI;

import javax.swing.*;
import java.awt.*;

public class CombatLogPanel extends JPanel {
    private final JTextArea logArea = new JTextArea(8, 20);

    public CombatLogPanel() {
        setLayout(new BorderLayout());
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced",Font.PLAIN,12));
        JScrollPane sp = new JScrollPane(logArea);
        sp.setBorder(BorderFactory.createTitledBorder("Combat Log"));
        add(sp, BorderLayout.CENTER);
    }

    public void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
