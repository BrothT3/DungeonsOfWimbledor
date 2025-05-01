// File: UI/GameUI.java
package UI;

import Cards.BaseCard;
import GameWorld.EquipmentManager;
import GameWorld.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameUI extends JPanel {
    private final CardPanel cardPanel;
    private final JLabel deckSizeDisplay;
    private final JLabel statsDisplay;
    private final JLabel equipmentDisplay;
    private final JLabel consumablesDisplay;
    private final JButton leftButton, upButton, rightButton;
    private Player player;

    public GameUI(Player player, BaseCard currentCard, List<BaseCard> deck) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
this.player = player;
        // ── NORTH: Player stats, equipment, consumables ─────────────────
        JPanel infoPanel = new JPanel(new GridLayout(3,1,5,5));
        infoPanel.setOpaque(false);
        statsDisplay       = new JLabel();
        equipmentDisplay   = new JLabel();
        consumablesDisplay = new JLabel();
        infoPanel.add(statsDisplay);
        infoPanel.add(equipmentDisplay);
        infoPanel.add(consumablesDisplay);

        // ── CENTER: Card + deck count ────────────────────────────────
        cardPanel = new CardPanel();
        deckSizeDisplay = new JLabel("Cards Left: " + deck.size(), SwingConstants.CENTER);
        deckSizeDisplay.setForeground(Color.LIGHT_GRAY);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(cardPanel, BorderLayout.CENTER);
        center.add(deckSizeDisplay, BorderLayout.SOUTH);

        // ── SOUTH: Swipe buttons (legacy EventCard support) ───────────
        JPanel swipePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        swipePanel.setOpaque(false);
        leftButton  = new JButton("← Left");
        upButton    = new JButton("↑ Up");
        rightButton = new JButton("→ Right");
        swipePanel.add(leftButton);
        swipePanel.add(upButton);
        swipePanel.add(rightButton);

        // ── Assemble ────────────────────────────────────────────────
        add(infoPanel,    BorderLayout.NORTH);
        add(center,       BorderLayout.CENTER);
        add(swipePanel,   BorderLayout.SOUTH);

        // ── NOW populate from model ─────────────────────────────────
        updateStats(player);
        updateEquipment(player);
        updateConsumables(player);
        updateCard(currentCard);
    }

    /** Refresh the big card display (title/text/buttons). */
    public void updateCard(BaseCard card) {
        cardPanel.updateCard(card, player);
    }

    /** Update the “Cards Left” label. */
    public void updateDeckSize(int size) {
        deckSizeDisplay.setText("Cards Left: " + size);
    }

    /** Show raw stats plus any gear bonuses. */
    public void updateStats(Player player) {
        // base stats (you can expand this)
        statsDisplay.setText(String.format(
                "HP: %d/%d  ATK: %d  DEF: %d  GOLD: %d",
                player.getHP(), player.getMaxHP(),
                player.getAttack(), player.getDefense(),
                player.getGold()
        ));
        statsDisplay.setForeground(Color.WHITE);
    }

    /** Show currently equipped items. */
    public void updateEquipment(Player player) {
        var em = EquipmentManager.GetInstance();
        String w = em.getWeapon()   != null ? em.getWeapon().getName()   : "None";
        String a = em.getArmor()    != null ? em.getArmor().getName()    : "None";
        String x = em.getAccessory()!= null ? em.getAccessory().getName(): "None";
        equipmentDisplay.setText(String.format(
                "Weapon: %s   Armor: %s   Accessory: %s", w, a, x
        ));
        equipmentDisplay.setForeground(Color.CYAN);
    }

    /** Show up to 3 consumables. */
    public void updateConsumables(Player player) {
        var list = EquipmentManager.GetInstance().getConsumables();
        String text = switch (list.size()) {
            case 0 -> "Consumables: (none)";
            case 1 -> "Consumables: " + list.get(0).getName();
            case 2 -> "Consumables: " + list.get(0).getName() + " | " + list.get(1).getName();
            default -> "Consumables: " + list.get(0).getName()
                    + " | " + list.get(1).getName()
                    + " | " + list.get(2).getName();
        };
        consumablesDisplay.setText(text);
        consumablesDisplay.setForeground(Color.ORANGE);
    }

    // Expose for controller wiring:
    public JButton getLeftButton()  { return leftButton; }
    public JButton getUpButton()    { return upButton; }
    public JButton getRightButton() { return rightButton; }
    public CardPanel getCardPanel(){ return cardPanel; }
}
