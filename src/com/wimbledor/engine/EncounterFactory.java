// src/main/java/com/wimbledor/engine/EncounterFactory.java
package com.wimbledor.engine;


import com.wimbledor.assets.encounterProviders.CrystalPoolEncounter;
import com.wimbledor.assets.encounterProviders.GoblinAmbushEncounter;
import com.wimbledor.assets.encounters.EncounterCard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds the full run by asking each provider to create its EncounterCard.
 */
public class EncounterFactory {
    private static final List<EncounterProvider> PROVIDERS = List.of(
            new CrystalPoolEncounter(),
            new GoblinAmbushEncounter()
            // add more EncounterProvider implementations here
    );

    public static List<EncounterCard> generateEncounters() {
        return PROVIDERS.stream()
                .map(EncounterProvider::create)
                .collect(Collectors.toList());
    }
}
