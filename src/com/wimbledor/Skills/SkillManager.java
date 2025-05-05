package com.wimbledor.skills;

import com.wimbledor.combat.CombatActions.ICombatAction;
import com.wimbledor.entities.ICombatEntity;
import com.wimbledor.entities.Player;

import java.util.Collections;
import java.util.List;

/**
 * Stub SkillManager for interim compilation.
 * Provides no skills by default and a no-op level-up hook.
 */
public class SkillManager {
    private static SkillManager instance;

    private SkillManager() {
        // private constructor
    }

    /**
     * Singleton accessor.
     */
    public static SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }
        return instance;
    }

    /**
     * Returns the list of combat actions (skills) available to the entity.
     * Currently returns an empty list as placeholder.
     */
    public List<ICombatAction> getSkills(ICombatEntity entity) {
        return Collections.emptyList();
    }

    /**
     * Hook for granting or improving skills on level-up. No-op currently.
     */
    public void onLevelUp(Player player, int newLevel) {
        // no skills to grant yet
    }
}