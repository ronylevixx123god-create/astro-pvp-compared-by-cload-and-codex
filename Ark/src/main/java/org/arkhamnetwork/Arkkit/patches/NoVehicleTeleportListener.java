/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerTeleportEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

final class NoVehicleTeleportListener
        implements Listener {
    NoVehicleTeleportListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportVehicle(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.getPlayer().getVehicle() != null && player.isInsideVehicle()) {
            player.sendMessage(ChatColor.RED + "You cannot teleport while you are inside a vehicle.");
            event.setCancelled(true);
        }
    }
}

