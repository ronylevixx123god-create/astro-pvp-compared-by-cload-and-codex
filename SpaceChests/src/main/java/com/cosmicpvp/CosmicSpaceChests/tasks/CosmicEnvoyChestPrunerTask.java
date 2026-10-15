/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.ArkhamEvents
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Chest
 *  org.bukkit.plugin.Plugin
 */
package com.cosmicpvp.CosmicSpaceChests.tasks;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import java.util.HashSet;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.plugin.Plugin;

public class CosmicEnvoyChestPrunerTask
implements Runnable {
    CosmicSpaceChestsPlugin plugin;

    public CosmicEnvoyChestPrunerTask(CosmicSpaceChestsPlugin fdp) {
        this.plugin = fdp;
    }

    @Override
    public void run() {
        HashSet<Location> chest_prunes = new HashSet<Location>();
        for (Map.Entry data : CosmicSpaceChestsPlugin.chest_locations.entrySet()) {
            Location l = (Location)data.getKey();
            long drop_time = (Long)data.getValue();
            if (System.currentTimeMillis() - drop_time < 300000L) continue;
            chest_prunes.add(l);
        }
        final Location[] loc_array = chest_prunes.toArray(new Location[chest_prunes.size()]);
        if (loc_array.length > 0) {
            CosmicSpaceChestsPlugin.log.debug("Removing " + loc_array.length + " drop packages with >180s lifespan", this.getClass());
            this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, new Runnable(){

                @Override
                public void run() {
                    for (Location l : loc_array) {
                        if (l.getBlock().getType() != Material.CHEST) continue;
                        Chest c = (Chest)l.getBlock().getState();
                        c.getBlockInventory().clear();
                        c.update();
                        l.getBlock().setType(Material.AIR);
                        CosmicSpaceChestsPlugin.chest_locations.removeChest(l);
                    }
                }
            });
        }
    }
}

