/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.listeners;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.events.PlayerEnterRegionEvent;
import com.compilex.CosmicDungeons.events.PlayerLeaveRegionEvent;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DungeonPlayerMoveListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getWorld().getName().startsWith("dungeon_")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!e.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        Event event;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        if (dp.getBukkitPlayer().hasMetadata("lastTrackedMoveEvent") && (long)MinecraftServerUtils.getCurrentServerTick() - ((MetadataValue)dp.getBukkitPlayer().getMetadata("lastTrackedMoveEvent").get(0)).asLong() < 20L) {
            return;
        }
        dp.getBukkitPlayer().setMetadata("lastTrackedMoveEvent", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)MinecraftServerUtils.getCurrentServerTick()));
        String lastRegion = dp.getBukkitPlayer().hasMetadata("lastRegionName") ? ((MetadataValue)dp.getBukkitPlayer().getMetadata("lastRegionName").get(0)).asString() : null;
        String currentRegion = WorldGuardUtils.getRegionName((Location)e.getTo());
        if (!(lastRegion == null || lastRegion.isEmpty() || currentRegion != null && currentRegion.equals(lastRegion))) {
            event = new PlayerLeaveRegionEvent(dp.getBukkitPlayer(), currentRegion);
            Bukkit.getPluginManager().callEvent(event);
        }
        if (!(lastRegion != null && lastRegion.equals(currentRegion) || currentRegion == null || currentRegion.length() <= 0)) {
            dp.getBukkitPlayer().setMetadata("lastRegionName", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)currentRegion));
            event = new PlayerEnterRegionEvent(dp.getBukkitPlayer(), currentRegion);
            Bukkit.getPluginManager().callEvent(event);
        } else if (currentRegion == null || currentRegion.isEmpty()) {
            dp.getBukkitPlayer().removeMetadata("lastRegionName", (Plugin)CosmicDungeonsPlugin.get_());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (!e.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        Event event;
        if (e.isCancelled()) {
            return;
        }
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        if (!e.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        String lastRegion = dp.getBukkitPlayer().hasMetadata("lastRegionName") ? ((MetadataValue)dp.getBukkitPlayer().getMetadata("lastRegionName").get(0)).asString() : null;
        String currentRegion = WorldGuardUtils.getRegionName((Location)e.getTo());
        if (!(lastRegion == null || lastRegion.isEmpty() || currentRegion != null && currentRegion.equals(lastRegion))) {
            event = new PlayerLeaveRegionEvent(dp.getBukkitPlayer(), currentRegion);
            Bukkit.getPluginManager().callEvent(event);
        }
        if (!(lastRegion != null && lastRegion.equals(currentRegion) || currentRegion == null || currentRegion.length() <= 0)) {
            dp.getBukkitPlayer().setMetadata("lastRegionName", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)currentRegion));
            event = new PlayerEnterRegionEvent(dp.getBukkitPlayer(), currentRegion);
            Bukkit.getPluginManager().callEvent(event);
        } else if (currentRegion == null || currentRegion.isEmpty()) {
            dp.getBukkitPlayer().removeMetadata("lastRegionName", (Plugin)CosmicDungeonsPlugin.get_());
        }
    }
}

