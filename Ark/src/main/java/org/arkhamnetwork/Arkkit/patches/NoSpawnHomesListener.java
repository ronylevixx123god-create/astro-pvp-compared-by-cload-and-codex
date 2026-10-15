/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

final class NoSpawnHomesListener
        implements Listener {
    NoSpawnHomesListener() {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (cmd.startsWith("/sethome") || cmd.startsWith("/esethome") || cmd.startsWith("/createhome") || cmd.startsWith("/ecreatehome") || cmd.startsWith("/f setwarp") || cmd.startsWith("/f sethome") || cmd.startsWith("/f setallywarp") || cmd.startsWith("/f addwarp") || cmd.startsWith("/f createwarp")) {
            if (e.getPlayer().isOp()) {
                return;
            }
            if (this.inSpawn(e.getPlayer().getLocation())) {
                e.setMessage("/null");
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot create a /home or /f warp at /spawn.");
                e.getPlayer().sendMessage(ChatColor.GRAY + "This is to prevent location exploitation when the spawn has theme changes.");
            } else {
                Bukkit.getLogger().info("[Arkkit (NoSpawnHomes)] Player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId() + ") issued command " + cmd + " at location " + e.getPlayer().getLocation());
            }
        }
    }

    private boolean inSpawn(Location l) {
        return WorldGuardUtils.getRegionName(l).equals("spawn_region");
    }
}

