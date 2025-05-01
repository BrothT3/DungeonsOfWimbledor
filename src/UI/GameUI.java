package UI;

import Cards.*;
import GameWorld.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class GameUI extends JPanel {
    private final CardPanel cardPanel = new CardPanel();
    private final JLabel deckSizeDisplay = new JLabel();
    private final JPanel playerStatsPanel = new JPanel(new GridLayout(0, 1));
    private final JLabel statsDisplay = new JLabel();

    private final JLabel weaponLabel = new JLabel();
    private final JLabel armorLabel = new JLabel();
    private final JLabel accessoryLabel = new JLabel();
    private final JLabel[] consumableLabels = new JLabel[3];

    public GameUI(Player player, BaseCard currentCard, List<BaseCard> remainingDeck) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // === CENTER (Card & Deck Info) ===
        updateCard(currentCard);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardPanel, BorderLayout.CENTER);

        deckSizeDisplay.setText("Cards Left: " + remainingDeck.size());
        deckSizeDisplay.setForeground(Color.LIGHT_GRAY);
        centerPanel.add(deckSizeDisplay, BorderLayout.SOUTH);

        // === RIGHT (Stats) ===
        playerStatsPanel.setOpaque(false);
        add(playerStatsPanel, BorderLayout.EAST);

        // === TOP (Equipment) ===
        JPanel equipmentPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        equipmentPanel.setOpaque(false);
        for (JLabel label : new JLabel[]{weaponLabel, armorLabel, accessoryLabel}) {
            label.setOpaque(true);
            label.setBackground(new Color(50, 50, 50));
            label.setForeground(Color.CYAN);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            equipmentPanel.add(label);
        }

        // === BOTTOM (Consumables) ===
        JPanel consumablesPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        consumablesPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            consumableLabels[i] = new JLabel("Empty", SwingConstants.CENTER);
            consumableLabels[i].setOpaque(true);
            consumableLabels[i].setBackground(new Color(40, 40, 40));
            consumableLabels[i].setForeground(Color.ORANGE);
            consumableLabels[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            consumablesPanel.add(consumableLabels[i]);
        }

        add(equipmentPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(consumablesPanel, BorderLayout.SOUTH);

        updateEquipment(player);
        updateConsumables(player);
    }

    public void updateCard(BaseCard card) {
        cardPanel.updateCard(card);
    }

    public void updateDeckSize(int size) {
        deckSizeDisplay.setText("Cards Left: " + size);
    }

    public void updateStats(Player player) {
        EquipmentManager em = EquipmentManager.GetInstance();
        playerStatsPanel.removeAll();

        int baseAtk = player.getAttack();
        int baseDef = player.getDefense();
        int baseEvasion = player.getEvasion();
        int baseSpeed = player.getSpeed();
        int baseAcc = player.getAccuracy();
        int baseCrit = player.getCritChance();
        int basePen = player.getPenetration();
        int baseCount = player.getAttackCount();

        int atkBonus = em.getWeapon() != null ? em.getWeapon().getAttackBonus() : 0;
        int defBonus = em.getArmor() != null ? em.getArmor().getDefenseBonus() : 0;
        int evaBonus = em.getArmor() != null ? em.getArmor().getEvasionBonus() : 0;
        int spdBonus = em.getArmor() != null ? em.getArmor().getSpeedBonus() : 0;
        int penBonus = em.getWeapon() != null ? em.getWeapon().getPenetrationBonus() : 0;
        int cntBonus = em.getWeapon() != null ? em.getWeapon().getAttackCountBonus() : 0;

        addStatLabel("HP: " + player.getHP() + "/" + player.getMaxHP());
        addStatLabel("Attack: " + baseAtk + (atkBonus > 0 ? " (+" + atkBonus + ")" : ""));
        addStatLabel("Defense: " + baseDef + (defBonus > 0 ? " (+" + defBonus + ")" : ""));
        addStatLabel("Penetration: " + basePen + (penBonus > 0 ? " (+" + penBonus + ")" : ""));
        addStatLabel("Attack Count: " + baseCount + (cntBonus > 0 ? " (+" + cntBonus + ")" : ""));
        addStatLabel("Speed: " + baseSpeed + (spdBonus > 0 ? " (+" + spdBonus + ")" : ""));
        addStatLabel("Evasion: " + baseEvasion + (evaBonus > 0 ? " (+" + evaBonus + ")" : ""));
        addStatLabel("Accuracy: " + baseAcc);
        addStatLabel("Crit Chance: " + baseCrit + "%");
        addStatLabel("Crit Damage: " + player.getCritDamage() + "x");
        addStatLabel("Gold: " + player.getGold());

        playerStatsPanel.revalidate();
        playerStatsPanel.repaint();
    }

    private void addStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        playerStatsPanel.add(label);
    }

    public void updateEquipment(Player player) {
        EquipmentManager em = EquipmentManager.GetInstance();

        var weapon = em.getWeapon();
        weaponLabel.setText("Weapon: " + (weapon != null ? weapon.getName() : "None"));
        weaponLabel.setToolTipText(weapon != null ? weapon.getDescription() : "");

        var armor = em.getArmor();
        armorLabel.setText("Armor: " + (armor != null ? armor.getName() : "None"));
        armorLabel.setToolTipText(armor != null ? armor.getDescription() : "");

        var accessory = em.getAccessory();
        accessoryLabel.setText("Accessory: " + (accessory != null ? accessory.getName() : "None"));
        accessoryLabel.setToolTipText(accessory != null ? accessory.getDescription() : "");
    }

    public void updateConsumables(Player player) {
        List<GameWorld.Equipment.Consumable> items = EquipmentManager.GetInstance().getConsumables();

        for (int i = 0; i < consumableLabels.length; i++) {
            JLabel label = consumableLabels[i];
            label.setText("Empty");
            label.setToolTipText("");
            for (MouseListener ml : label.getMouseListeners()) {
                label.removeMouseListener(ml);
            }

            if (i < items.size()) {
                GameWorld.Equipment.Consumable item = items.get(i);
                label.setText(item.getName());
                label.setToolTipText(item.getDescription());

                final int index = i;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        EquipmentManager.GetInstance().useConsumable(index, player);
                        updateConsumables(player);
                        updateStats(player);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        label.setBackground(new Color(60, 60, 60));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        label.setBackground(new Color(40, 40, 40));
                    }
                });
            } else {
                label.setText("Empty");
                label.setToolTipText("");
            }
        }
    }

    public CardPanel GetCardPanel() {
        return cardPanel;
    }
}