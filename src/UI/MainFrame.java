package UI;

import Cards.*;
import GameWorld.*;
import GameWorld.Equipment.Accessories.CrudWithString;
import GameWorld.Equipment.Armors.CruddyTrousers;
import GameWorld.Equipment.Weapons.CruddySword;
import GameWorld.Interfaces.GameEngine;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame implements GameEngine {
    private final Player player;
    private final WorldManager world;
    private BaseCard currentCard;
    private TurnManager turnManager;

    // UI sub‐panels:
    private final StatsPanel         statsPanel         = new StatsPanel();
    private final EquipmentPanel     equipmentPanel     = new EquipmentPanel();
    private final ConsumablesPanel   consumablesPanel   = new ConsumablesPanel();
    private final CardDisplayPanel   cardDisplayPanel   = new CardDisplayPanel();
    private final ControlPanel       controlPanel       = new ControlPanel();
    private final CombatLogPanel     combatLogPanel     = new CombatLogPanel();
    private BattleCard currentBattle;

    public MainFrame() {
        super("Dungeon of Wimbledor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        // ─── 1) Init model ────────────────────────────────────────────────────
        player = new Player();
        world  = new WorldManager();
        world.generateDeck();
        currentCard = world.drawCard();
        currentCard.setEngine(this);
        // starter gear
        var em = EquipmentManager.GetInstance();
        em.equipWeapon   (new CruddySword(),    player);
        em.equipArmor    (new CruddyTrousers(), player);
        em.equipAccessory(new CrudWithString(), player);

        // ─── 2) Build UI ─────────────────────────────────────────────────────
        JPanel leftPane = new JPanel(new BorderLayout(5,5));
        leftPane.add(cardDisplayPanel, BorderLayout.CENTER);
        leftPane.add(controlPanel,     BorderLayout.SOUTH);

        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
        rightPane.add(statsPanel);
        rightPane.add(Box.createVerticalStrut(5));
        rightPane.add(equipmentPanel);
        rightPane.add(Box.createVerticalStrut(5));
        rightPane.add(consumablesPanel);
        rightPane.add(Box.createVerticalStrut(5));
        rightPane.add(combatLogPanel);

        add(leftPane, BorderLayout.CENTER);
        add(rightPane, BorderLayout.EAST);

        // ─── 3) Wire callbacks ───────────────────────────────────────────────
        controlPanel.setEncounterHandler(this::handleEncounterChoice);
        controlPanel.setCombatHandler   (this::handleCombatAction);

        // ─── 4) Initial paint ────────────────────────────────────────────────
        refreshAll();
        setVisible(true);
    }

    private void refreshAll() {
        cardDisplayPanel.updateCard(currentCard, player, turnManager);
        statsPanel.updateStats(player);
        equipmentPanel.updateEquipment(player);
        consumablesPanel.updateConsumables(player);
        controlPanel.updateControls(currentCard, player, turnManager);
        combatLogPanel.repaint();
    }

    private void handleEncounterChoice(String code) {
        if (!(currentCard instanceof EncounterCard ec)) {
            return;
        }
        ec.onInteract(player, code);
        // ec.onInteract will call back to startBattle(...) or onEncounterComplete()
        refreshAll();
    }
    private void handleCombatAction(ICombatAction action) {
        List<ICombatEntity> targets = determineTargets(action);
        action.execute(player, targets);
        combatLogPanel.log(player.getName()
            + " uses " + action.getLabel()
            + " on " + targets.stream().map(ICombatEntity::getName).collect(Collectors.joining(", "))
        );
        turnManager.startNextTurn();
        if (turnManager.isBattleOver()) {
            onBattleOver();
        } else {
            ICombatEntity next = turnManager.getCurrentEntity();
            if (!(next instanceof Player)) {
                ICombatAction aiAction = next.getActions().get(0);
                List<ICombatEntity> aiTargets = determineTargets(aiAction);
                aiAction.execute(next, aiTargets);
                combatLogPanel.log(next.getName() 
                  + " uses " + aiAction.getLabel()
                  + " on " + aiTargets.stream().map(ICombatEntity::getName).collect(Collectors.joining(", "))
                );
                turnManager.startNextTurn();
            }
        }
        refreshAll();
    }

    private List<ICombatEntity> determineTargets(ICombatAction action) {
        switch (action.getTargetMode()) {
            case SELF:
                return List.of(player);
            case ALL_ENEMIES:
                return turnManager.getEnemiesOf(player.getTeam());
            case SINGLE_ENEMY:
                var enemies = turnManager.getEnemiesOf(player.getTeam());
                return enemies.size()==1 ? enemies : List.of(enemies.get(0));
            default:
                return List.of(player);
        }
    }

    private void nextCard() {
        currentCard = world.drawCard();
        currentCard.setEngine(this);
        if (currentCard instanceof MonsterCard mc) {
            turnManager = new TurnManager(List.of(player, mc));
            turnManager.startNextTurn();
            if (!(turnManager.getCurrentEntity() instanceof Player)) {
                CombatUtils.entityTurn(mc, player);
                turnManager.startNextTurn();
            }
        } else {
            turnManager = null;
        }
    }

    private void onBattleOver() {
        if (turnManager.getCurrentEntity().getHP() <= 0) {
            JOptionPane.showMessageDialog(this, "You died!");
            System.exit(0);
        }
        var mc = (MonsterCard)currentCard;
        player.addGold(mc.getGold());
        nextCard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }

    @Override
    public void startBattle(BattleCard battle, TurnManager tm) {
        this.currentBattle = battle;
        this.turnManager    = tm;
        // replace the “current card” with this battle:
        this.currentCard    = battle;
        refreshUI();
        // kick off first monster turn if needed:
        if (! (tm.getCurrentEntity() instanceof Player)) {
            CombatUtils.entityTurn(tm.getCurrentEntity(), player);
            tm.startNextTurn();
            refreshUI();
        }
    }

    @Override
    public void onEncounterComplete() {
        // your existing logic that draws the next card, etc.
        nextCard();
    }

    @Override
    public void refreshUI() {
        // 1) Show the current card (Encounter, Battle or old Event)
        cardDisplayPanel.updateCard(currentCard, player, turnManager);

        // 2) Update the buttons under the card:
        //    • If it's an encounter, you get its StageOptions.
        //    • If you're mid‐combat and it's the player's turn, you get their actions.
        //    • Otherwise (legacy) you get the three swipe buttons.
        controlPanel.updateControls(currentCard, player, turnManager);

        // 3) Update the numeric stats (HP, ATK, DEF, SPD, etc)
        statsPanel.updateStats(player);

        // 4) Show what the player’s got equipped right now
        equipmentPanel.updateEquipment(player);

        // 5) Show the 3 consumable slots & wire their click handlers
        consumablesPanel.updateConsumables(player);

        // 6) (Optional) scroll the combat log to the bottom so you always see the latest entry
        combatLogPanel.scrollToBottom();

    }

}

