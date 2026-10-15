/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

final class DisguiseCommandListener
        implements Listener {
    DisguiseCommandListener() {
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (cmd.startsWith("/disguise") && cmd.contains(" ") && cmd.split(" ").length >= 3 && cmd.split(" ")[1].equals("player")) {
            String disguiseName = e.getMessage().split(" ")[2];
            if (Bukkit.getPlayer(disguiseName) != null) {
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot disguise as a player that is already online.");
                e.setCancelled(true);
                return;
            }
            Bukkit.getLogger().info("[DisguiseCommandListener] Logged " + e.getPlayer().getName() + "'s disguiseName as " + disguiseName);
        }
    }
}

