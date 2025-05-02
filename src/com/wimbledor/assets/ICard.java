package com.wimbledor.assets;

import java.util.List;

public interface ICard {
    String getTitle();

    String getDescription();

    List<CardOption> getOptions();

    /**
     * Called when the player chooses an option.
     *
     * @param code the unique code of the chosen option
     * @return the “next” card to display (null if deck should draw)
     */
    ICard onOptionSelected(String code);
}