/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.cosmicpvp.cosmicutils.inventory.rewards;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RewardsExampleUsage
        extends JavaPlugin {
    private SerializedRewards rewards;

    public void onEnable() {
        this.rewards = new SerializedRewards(this);
        this.rewards.load();
    }

    public void onDisable() {
        this.rewards.save();
    }

    public void openRewards(Player player) {
        this.rewards.open(player);
    }
}

