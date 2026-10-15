/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.filoghost.holograms.api.Hologram
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.cosmicpvp.CosmicSpaceChests.tasks;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyTask;
import com.cosmicpvp.CosmicSpaceChests.utils.LocationUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramUpdateTask
extends BukkitRunnable {
    private Location hologramLocation = null;
    private Hologram spawnHologram = null;
    private boolean enabled = true;

    public void run() {
        if (!this.enabled) {
            return;
        }
        if (this.hologramLocation == null) {
            Location location = this.hologramLocation = CosmicSpaceChestsPlugin.plugin.getConfig().contains("hologram-location") ? LocationUtils.convertStringToLocation(CosmicSpaceChestsPlugin.plugin.getConfig().getString("hologram-location")) : null;
            if (this.hologramLocation == null) {
                return;
            }
        }
        if (this.spawnHologram != null && !this.spawnHologram.isDeleted()) {
            this.spawnHologram.delete();
        }
    }

    public void setHologramLocation(Location l) {
        this.hologramLocation = l;
        CosmicSpaceChestsPlugin.plugin.getConfig().set("hologram-location", (Object)LocationUtils.convertLocationToString(l));
        CosmicSpaceChestsPlugin.plugin.saveConfig();
        if (this.spawnHologram != null) {
            this.spawnHologram.delete();
            this.spawnHologram = null;
        }
    }

    public void disableHologram() {
        this.enabled = false;
        if (this.spawnHologram != null && !this.spawnHologram.isDeleted()) {
            this.spawnHologram.delete();
        }
    }

    public void enableHologram() {
        this.enabled = true;
    }
}

