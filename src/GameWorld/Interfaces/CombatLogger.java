package GameWorld;

import java.awt.Color;

@FunctionalInterface
public interface CombatLogger {
    /**
     * @param message   the line to append to the log
     * @param flashColor if non-null, the UI can flash that color
     */
    void log(String message, Color flashColor);
}