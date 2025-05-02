package com.wimbledor.assets;

import com.wimbledor.engine.GameContext;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCard implements ICard {
    private final String title;
    private final String description;
    private final List<CardOption> options = new ArrayList<>();

    protected AbstractCard(String title, String description) {
        this.title = title;
        this.description = description;
        defineOptions();   // hook for subclasses
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<CardOption> getOptions() {
        return List.copyOf(options);
    }

    /**
     * Subclasses implement to populate options via addOption(...)
     */
    protected abstract void defineOptions();

    protected void addOption(CardOption option) {
        options.add(option);
    }

    @Override
    public ICard onOptionSelected(String code) {
        for (CardOption opt : options) {
            if (opt.getCode().equals(code)) {
                opt.applyEffect(GameContext.getPlayer());
                return opt.getNextCard();
            }
        }
        throw new IllegalArgumentException("Invalid option code: " + code);
    }
}