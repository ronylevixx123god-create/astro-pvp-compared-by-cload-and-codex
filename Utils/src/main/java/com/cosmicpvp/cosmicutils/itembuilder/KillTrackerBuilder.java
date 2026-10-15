/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  me.ifamasssxd.killtracker.me.astropvp.cratekeys.net.astropvp.armorsets.listeners.Tracker
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.struct.ItemTier;
import me.ifamasssxd.killtracker.KillTrackerAPI;
import me.ifamasssxd.killtracker.listeners.Tracker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class KillTrackerBuilder
        extends ItemBuilder {
    private ItemTier tier;
    private int min;
    private int max;

    public KillTrackerBuilder(ItemTier tier) {
        super(Material.PAPER);
        this.tier = tier;
    }

    public KillTrackerBuilder(ItemTier tier, int min, int max) {
        super(Material.PAPER);
        this.tier = tier;
        this.min = min;
        this.max = max;
    }

    @Override
    public ItemStack build() {
        Tracker type = Arrays.stream(Tracker.values()).filter(tra -> tra.getRank() == this.tier.getRarityId()).findFirst().orElse(Tracker.Simple);
        ItemStack retr = KillTrackerAPI.buildTracker((Tracker) type);
        if (this.min != 0) {
            retr.setAmount(ThreadLocalRandom.current().nextInt(this.max - this.min + 1) + this.min);
        }
        return retr;
    }
}

