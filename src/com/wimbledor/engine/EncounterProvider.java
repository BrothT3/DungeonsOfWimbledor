// src/main/java/com/wimbledor/engine/encounters/EncounterProvider.java
package com.wimbledor.engine;

import com.wimbledor.assets.encounters.EncounterCard;

/**
 * Implement this to provide one EncounterCard.
 */
public interface EncounterProvider {
    EncounterCard create();
}
