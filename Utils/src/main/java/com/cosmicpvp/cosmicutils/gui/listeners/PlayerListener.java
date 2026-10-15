package com.cosmicpvp.cosmicutils.gui.listeners;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener
        implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerLoginEvent e) {
        CosmicUtils.get().getAsyncPlayerMap().put(e.getPlayer().getName(), e.getPlayer());
        CosmicUtils.get().getAsyncPlayerMapUUID().put(e.getPlayer().getUniqueId(), e.getPlayer());
        CosmicUtils.get().getPlayerLocations().put(e.getPlayer().getName(), e.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        CosmicUtils.get().getAsyncPlayerMap().remove(e.getPlayer().getName());
        CosmicUtils.get().getAsyncPlayerMapUUID().remove(e.getPlayer().getUniqueId());
        CosmicUtils.get().getPlayerLocations().remove(e.getPlayer().getName());
        WorldGuardUtils.clearCachedRegions(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        String ownerUUID;
        long time;
        if (event.getItem().hasMetadata("protectedItem") && ((time = event.getItem().getMetadata("protectedItem").get(0).asLong()) == -1L || time > System.currentTimeMillis()) && !(ownerUUID = event.getItem().getMetadata("protectedOwner").get(0).asString()).equals(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPreProcessCommand(PlayerCommandPreprocessEvent event) {
        String lower = event.getMessage().toLowerCase();
        if (lower.startsWith("/rg define") || lower.startsWith("/rg redefine") && event.getPlayer().isOp()) {
            Bukkit.getLogger().info("Reloading regions due to /rg updating command!");
            WorldGuardUtils.cacheAllRegions();
        }
    }
}

