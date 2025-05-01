// File: UI/GameFrame.java
package UI;

import Cards.BaseCard;
import Cards.BattleCard;
import Cards.EncounterCard;
import Cards.EventCard;
import GameWorld.*;
import GameWorld.Equipment.Accessories.CrudWithString;
import GameWorld.Equipment.Armors.CruddyTrousers;
import GameWorld.Equipment.Weapons.CruddySword;
import GameWorld.Interfaces.ICombatEntity;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class GameFrame extends JFrame {
    private static GameFrame INSTANCE;
    public static GameFrame getInstance() { return INSTANCE; }

    private final Player       player;
    private final WorldManager worldManager;
    private       TurnManager  turnManager;
    private       BaseCard     currentCard;
    private final GameUI       gameUI;

    public GameFrame() {
        super("Dungeons of Wimbledor");
        INSTANCE = this;

        // 0) Window setup
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);

        // 1) Core model init
        player       = new Player();
        worldManager = new WorldManager();
        worldManager.generateDeck();         // fill & shuffle
        currentCard  = worldManager.drawCard();

        // 2) Starter gear
        var em = EquipmentManager.GetInstance();
        em.equipWeapon(new CruddySword(), player);
        em.equipArmor (new CruddyTrousers(),player);
        em.equipAccessory(new CrudWithString(),player);

        // 3) Build UI
        gameUI = new GameUI(player, currentCard, worldManager.getDeck());
        setContentPane(gameUI);

        // 4) Wire up keys & swipes
        setupListeners();

        // 5) First full sync
        updateUI();

        // 6) Show
        setVisible(true);
    }

    /** Single method to refresh *everything* on screen. */
    public void updateUI() {
        // a) If we just landed on a BattleCard and haven't started combat yet, do so
        if (currentCard instanceof BattleCard bc && turnManager == null) {
            startBattle(bc);
            return;
        }

        // b) Update card / stats / deck / gear / consumables
        gameUI.updateCard(currentCard);
        gameUI.updateDeckSize(worldManager.getDeckSize());
        gameUI.updateStats(player);
        gameUI.updateEquipment(player);
        gameUI.updateConsumables(player);

        // c) If mid-combat & it’s player’s turn, re-show actions
        if (turnManager != null
                && turnManager.getCurrentEntity() instanceof Player) {
            gameUI.getCardPanel().showActions(player);
        }

        gameUI.requestFocusInWindow();
    }

    /** When a BattleCard triggers, spin up combat and flash the buttons. */
    public void startBattle(BattleCard bc) {
        // 1) keep reference
        currentCard = bc;

        // 2) build turn queue
        List<ICombatEntity> combatants = new ArrayList<>();
        combatants.add(player);
        combatants.addAll(bc.getMonsters());

        turnManager = new TurnManager(combatants);
        turnManager.startNextTurn();

        // 3) faster side opening strike
        if (!(turnManager.getCurrentEntity() instanceof Player)) {
            var enemy = turnManager.getCurrentEntity();
            CombatUtils.entityTurn(enemy, player);
            turnManager.startNextTurn();
        }

        // 4) show UI + player buttons
        gameUI.updateCard(currentCard);
        gameUI.updateDeckSize(worldManager.getDeckSize());
        gameUI.updateStats(player);
        gameUI.updateEquipment(player);
        gameUI.updateConsumables(player);
        gameUI.getCardPanel().showActions(player);
    }

    /** Handle a swipe or button click in legacy EventCard flow. */
    public void processAction(String code) {
        if (currentCard instanceof EventCard ev) {
            ev.onInteract(player, code);
            currentCard = worldManager.drawCard();
            turnManager = null;      // cancel any old combat
            updateUI();
        }
    }

    private void setupListeners() {
        // Arrow keys & 'S' for swipes
        gameUI.getLeftButton().addActionListener(e -> processAction("LEFT"));
        gameUI.getUpButton().addActionListener(e -> processAction("UP"));
        gameUI.getRightButton().addActionListener(e -> processAction("RIGHT"));

        gameUI.addKeyListener(new KeyAdapter(){
            @Override public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> processAction("LEFT");
                    case KeyEvent.VK_UP    -> processAction("UP");
                    case KeyEvent.VK_RIGHT -> processAction("RIGHT");
                    case KeyEvent.VK_S     -> processAction("LEFT");
                }
            }
        });
        gameUI.setFocusable(true);
    }
    public void onBattleComplete() {
        // 1) award all gold
        BattleCard bc = (BattleCard)currentCard;
        bc.getMonsters().forEach(m -> player.addGold(m.getGold()));

        // 2) if that battle was nested inside an EncounterCard, advance it
        if (bc.getParentEncounter() instanceof EncounterCard enc) {
            enc.advanceTo(bc.getPostBattleStage());
            currentCard = enc;
            // call back into updateUI so that the new encounter stage shows up:
            updateUI();
        }
        // 3) otherwise just draw a fresh world card
        else {
            turnManager = null;
            currentCard = worldManager.drawCard();
            updateUI();
        }
    }

    /** Called when an EncounterCard has run out of stages. */
    public void onEncounterComplete() {
        turnManager = null;
        currentCard = worldManager.drawCard();
        updateUI();
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }

    public GameUI getGameUI() {
        return gameUI;
    }
}
