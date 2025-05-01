package GameWorld;

import Cards.MonsterCard;
import GameWorld.Interfaces.ICombatEntity;
import UI.CardPanel;
import UI.GameFrame;
import UI.GameUI;

import java.awt.*;
import java.util.Random;

public class CombatUtils {

    private static final Random random = new Random();

    public static void entityTurn(ICombatEntity attacker, ICombatEntity defender) {
        for (int i = 0; i < attacker.getAttackCount(); i++) {
            boolean hit = didHit(attacker.getAccuracy(), defender.getEvasion());

            if (hit) {
                boolean crit = random.nextFloat() < (attacker.getCritChance() / 100f);
                int baseDamage = calculateDamage(attacker.getAttack(), defender.getDefense(), attacker.getPenetration());
                int finalDamage = crit ? Math.round(baseDamage * attacker.getCritDamage()) : baseDamage;

                defender.takeDamage(finalDamage);

                String msg = getName(attacker) + " hit " + getName(defender) +
                        " for " + finalDamage + (crit ? " (CRIT!)" : "");
                Color flashColor = crit
                        ? Color.YELLOW
                        : (attacker instanceof Player ? Color.GREEN : Color.RED);

                logToUI(msg, flashColor);
            } else {
                String msg = getName(attacker) + " missed " + getName(defender) + "!";
                logToUI(msg, Color.LIGHT_GRAY);
            }
        }
    }

    public static void defendAction(ICombatEntity entity) {
        if (entity instanceof Player player) {
            player.setTempDefenseBoost(5); //should be made to disappear eventuelt
            logToUI("Player braces for impact!", Color.CYAN);
        } else {
            logToUI(getName(entity) + " defends!", Color.CYAN);
        }
    }

    public static void accessoryAction(Player player) {
        if (player.hasAccessoryAction()) {
            player.useAccessoryAction();
            logToUI("Player used their accessory!", Color.MAGENTA);
        } else {
            logToUI("Player skipped their turn.");
        }
    }

    // === Utility ===

    private static boolean didHit(int accuracy, int evasion) {
        int hitChance = Math.max(10, Math.min(90, accuracy - evasion));
        return random.nextInt(100) < hitChance;
    }

    private static int calculateDamage(int attack, int defense, int penetration) {
        int effectiveDefense = Math.max(0, defense - penetration);
        return Math.max(1, attack - effectiveDefense);
    }

    private static String getName(ICombatEntity entity) {
        if (entity instanceof Player) return "Player";
        if (entity instanceof MonsterCard m) return m.getTitle();
        return "Entity";
    }

    public static void logToUI(String message) {
        logToUI(message, null);
    }

    private static void logToUI(String message, Color flashColor) {
        GameFrame frame = GameFrame.getInstance();
        if (frame == null) return;
        GameUI ui = frame.getGameUI();
        if (ui == null) return;

        CardPanel panel = ui.getCardPanel();
        panel.log(message);
        if (flashColor != null) panel.flash(flashColor);
    }
}
