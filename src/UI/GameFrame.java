package UI;

import Cards.BaseCard;
import Cards.EventCard;
import Cards.MonsterCard;
import GameWorld.CombatUtils;
import GameWorld.EquipmentManager;
import GameWorld.Equipment.Weapons.CruddySword;
import GameWorld.Equipment.Armors.CruddyTrousers;
import GameWorld.Equipment.Accessories.CrudWithString;
import GameWorld.Player;
import GameWorld.TurnManager;
import GameWorld.WorldManager;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GameFrame extends JFrame {
    private static GameFrame INSTANCE;
    public static GameFrame getInstance() { return INSTANCE; }

    private final Player       player;
    private final WorldManager worldManager;
    private GameUI       gameUI;
    private       TurnManager  turnManager;
    public GameUI getGameUI(){
        return  gameUI;
    }

    public GameFrame() {
        super("Dungeons of Wimbledor");
        INSTANCE = this;

        // Window setup
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        //  Initialize game logic
        player       = new Player();
        worldManager = new WorldManager();

        // Starter grej
        var em = EquipmentManager.GetInstance();
        em.equipWeapon   (new CruddySword(),    player);
        em.equipArmor    (new CruddyTrousers(), player);
        em.equipAccessory(new CrudWithString(), player);

        //  Async deck generation + UI creation
        worldManager.generateDeckAsync(() -> {
            drawNextCard();


            gameUI = new GameUI(
                    player,
                    worldManager.getCurrentCard(),
                    worldManager.getDeck()
            );
            setContentPane(gameUI);

            //  make listeners
            setupListeners();

            // d) full UI sync
            updateUI();

            // e) show the window
            setVisible(true);
        });
    }

    private void drawNextCard() {
        BaseCard next = worldManager.drawCard();
        if (next == null) {
            worldManager.nextLevel();
            next = worldManager.drawCard();
        }
        if (next instanceof MonsterCard m) {
            turnManager = new TurnManager(List.of(player, m));
            turnManager.startNextTurn();
            if (!(turnManager.getCurrentEntity() instanceof Player)) {
                CombatUtils.entityTurn(m, player);
                turnManager.startNextTurn();
            }
        } else {
            turnManager = null;
        }
    }

    private void setupListeners() {
        gameUI.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> processEventSwipe("LEFT");
                    case KeyEvent.VK_UP    -> processEventSwipe("UP");
                    case KeyEvent.VK_RIGHT -> processEventSwipe("RIGHT");
                    case KeyEvent.VK_S     -> shakeCard();
                }
            }
        });
        gameUI.setFocusable(true);
        gameUI.requestFocusInWindow();
    }

    public void processAction(String dir){
        processEventSwipe(dir);
    }
    private void processEventSwipe(String dir) {
        BaseCard c = worldManager.getCurrentCard();
        if (c instanceof EventCard ev) {
            ev.applyEffect(player, dir);
            drawNextCard();
            updateUI();
        }
    }

    /** Fling away a card (Event only) if shakes remain. */
    private void shakeCard() {
        if (player.getShakes() > 0) {
            player.setShakes(player.getShakes() - 1);
            drawNextCard();
            updateUI();
        }
    }

    /** Refreshes the card text, combat buttons, stats, deck count, etc. */
    public void updateUI() {
        gameUI.updateCard       (worldManager.getCurrentCard());
        gameUI.updateDeckSize   (worldManager.getDeck().size());
        gameUI.updateStats      (player);
        gameUI.updateEquipment  (player);
        gameUI.updateConsumables(player);
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }


    public void handleBattleEnd() {
        if (player.getHP() <= 0) {
            JOptionPane.showMessageDialog(this, "Game Over!");
            System.exit(0);
        } else {
            MonsterCard m = (MonsterCard) worldManager.getCurrentCard();
            player.addGold(m.getGold());
            drawNextCard();
            updateUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
