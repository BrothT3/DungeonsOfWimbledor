package GameWorld;

import Cards.MonsterCard;
import GameWorld.Interfaces.ICombatEntity;
import UI.*;
import GameWorld.CombatLogger;

import java.awt.*;
import java.util.Random;

public class CombatUtils {

    private static final Random RANDOM = new Random();

    // ← this is your new back‐door into the UI
    private static CombatLogger logger = (msg, col) -> {};

    /** Call once at startup to wire up your UI. */
    public static void setLogger(CombatLogger l) {
        logger = l != null ? l : (m, c) -> {};
    }

    public static void entityTurn(ICombatEntity attacker, ICombatEntity defender) {
        for (int i = 0; i < attacker.getAttackCount(); i++) {
            boolean hit = didHit(attacker.getAccuracy(), defender.getEvasion());
            if (hit) {
                boolean crit = RANDOM.nextFloat() < attacker.getCritChance()/100f;
                int dmg0 = calculateDamage(
                        attacker.getAttack(), defender.getDefense(), attacker.getPenetration()
                );
                int finalDmg = crit
                        ? Math.round(dmg0 * attacker.getCritDamage())
                        : dmg0;
                defender.takeDamage(finalDmg);

                String line = getName(attacker) + " hit " + getName(defender) +
                        " for " + finalDmg + (crit ? " (CRIT!)" : "");
                Color flash = crit
                        ? Color.YELLOW
                        : (attacker instanceof Player ? Color.GREEN : Color.RED);
                logger.log(line, flash);
            } else {
                logger.log(getName(attacker) + " missed " + getName(defender) + "!",
                        Color.LIGHT_GRAY);
            }
        }
    }

    public static void defendAction(ICombatEntity entity) {
        if (entity instanceof Player p) {
            p.setTempDefenseBoost(5);
            logger.log("Player braces for impact!", Color.CYAN);
        } else {
            logger.log(getName(entity) + " defends!", Color.CYAN);
        }
    }

    public static void accessoryAction(Player player) {
        if (player.hasAccessoryAction()) {
            player.useAccessoryAction();
            logger.log("Player used their accessory!", Color.MAGENTA);
        } else {
            logger.log("Player skipped their turn.", null);
        }
    }

    // === Utility ===

    private static boolean didHit(int accuracy, int evasion) {
        int hitChance = Math.max(10, Math.min(90, accuracy - evasion));
        return RANDOM.nextInt(100) < hitChance;
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


}
