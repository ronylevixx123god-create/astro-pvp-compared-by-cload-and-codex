/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.vehicle.VehicleCreateEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

final class BoatBlockerListener
        implements Listener {
    BoatBlockerListener() {
    }

    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent e) {
        if (e.getVehicle() instanceof Boat) {
            e.getVehicle().remove();
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        if (e.hasItem() && e.getItem().getType() == Material.BOAT && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            pl.sendMessage(ChatColor.RED + "Boat placement is currently " + ChatColor.UNDERLINE + "disabled.");
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
        }
    }
}

