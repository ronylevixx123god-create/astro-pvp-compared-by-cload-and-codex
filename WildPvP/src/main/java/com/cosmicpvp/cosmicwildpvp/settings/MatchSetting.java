package com.cosmicpvp.cosmicwildpvp.settings;

import org.bukkit.inventory.ItemStack;

public abstract class MatchSetting
{
    private String key;

    public MatchSetting(final String key) {
        this.key = key;
    }

    public abstract ItemStack getMatchItem(final boolean p0);

    public String getKey() {
        return this.key;
    }
}
