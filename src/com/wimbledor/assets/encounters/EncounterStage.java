// src/com/wimbledor/cards/encounters/EncounterStage.java
package com.wimbledor.assets.encounters;

import java.util.List;

/**
 * A single “page” in an encounter: description + options.
 */
public class EncounterStage {
    private final String description;
    private final List<StageOption> options;

    public EncounterStage(String description, List<StageOption> options) {
        this.description = description;
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public List<StageOption> getOptions() {
        return options;
    }
}