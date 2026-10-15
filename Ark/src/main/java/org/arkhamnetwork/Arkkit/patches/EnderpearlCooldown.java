/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.UpgradeUtils
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class EnderpearlCooldown
        extends ArkPlugin {
    private static int enderpearlDelayMilliseconds = 10000;
    private static int arenaDelayMilliseconds = 30000;
    private static int kothDelayMilliseconds;
    public boolean useHCFScoreboard = false;

    public EnderpearlCooldown(Plugin p) {
        super(p);
        enderpearlDelayMilliseconds = Arkkit.get().getConfig().getInt("settings.enderpearlCooldown.cooldown");
        kothDelayMilliseconds = Arkkit.get().getConfig().getInt("settings.enderpearlCooldown.kothCooldown");
        arenaDelayMilliseconds = Arkkit.get().getConfig().getInt("settings.enderpearlCooldown.arenaCooldown");
        this.useHCFScoreboard = Arkkit.get().getConfig().getBoolean("settings.enderpearlCooldown.hcfScoreboard", false);
    }

    public static long getEnderPearlCooldown(Player pl) {
        long retr = pl.getWorld().getName().equals("world_arena") ? arenaDelayMilliseconds : (pl.getWorld().getName().equals("world_koth") ? kothDelayMilliseconds : enderpearlDelayMilliseconds);

        if (pl.getWorld().getName().contains("dungeon"))
            return 500L;
        return retr;
    }

    public static boolean isEnderpearlCooldownOver(Player pl) {
        long last_throw = pl.hasMetadata("last_enderpearl_throw") ? pl.getMetadata("last_enderpearl_throw").get(0).asLong() : 0L;
        return last_throw <= 0L || System.currentTimeMillis() - last_throw > EnderpearlCooldown.getEnderPearlCooldown(pl);
    }

    public static int getEnderpearlCooldownLeft(Player pl) {
        return (int) EnderpearlCooldown.getEnderpearlCooldownLeftDouble(pl);
    }

    public static double getEnderpearlCooldownLeftDouble(Player pl) {
        long cd;
        long last_throw;
        long l = last_throw = pl.hasMetadata("last_enderpearl_throw") ? pl.getMetadata("last_enderpearl_throw").get(0).asLong() : 0L;
        if (last_throw <= 0L) {
            return 0.0;
        }
        double dif = System.currentTimeMillis() - last_throw;
        return dif < (double) (cd = EnderpearlCooldown.getEnderPearlCooldown(pl)) ? (double) cd - dif : 0.0;
    }

    @Override
    public void enable() {
        this.registerListener(new EnderpearlCooldownListener(this));
        if (new File("plugins/WorldBorder.jar").exists()) {
        }
    }

    @Override
    public void disable() {
        this.unregisterListeners();
    }
}

