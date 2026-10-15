/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.WorldLoadEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

final class BulkSavingListener
        implements Listener {
    BulkSavingListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent e) {
        World w = e.getWorld();
        BulkSaving.default_save_periods.put(w, ((CraftWorld) w).getHandle().getServer().getServer().autosavePeriod);
        ((CraftWorld) w).getHandle().getServer().getServer().autosavePeriod = 0;
        w.setAutoSave(false);
    }
}

