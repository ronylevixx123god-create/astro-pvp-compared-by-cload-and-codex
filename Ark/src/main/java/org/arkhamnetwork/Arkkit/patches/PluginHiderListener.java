/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

final class PluginHiderListener
        implements Listener {
    PluginHiderListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCMD(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().contains(":")) {
            return;
        }
        String[] message = event.getMessage().split(":");
        if (message[0] != null) {
            for (Plugin plugin : Arkkit.get().getServer().getPluginManager().getPlugins()) {
                if (!plugin.getName().toLowerCase().equalsIgnoreCase(message[0].toLowerCase().replace("/", "")))
                    continue;
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        cmd = cmd.replace("/", "");
        Player pl = e.getPlayer();
        if (cmd.startsWith("bukkit:") && !pl.isOp()) {
            e.setCancelled(true);
            pl.sendMessage(ChatColor.RED + "The command " + cmd + " is unavailable.");
            return;
        }
        if (!pl.isOp() && (cmd.toLowerCase().startsWith("?") || cmd.toLowerCase().startsWith("?") || cmd.equalsIgnoreCase("pl") || cmd.toLowerCase().startsWith("pl ") || cmd.toLowerCase().startsWith("plugins"))) {
            e.setCancelled(true);
            pl.sendMessage(ChatColor.RED + "The command " + cmd + " is unavailable.");
            return;
        }
    }
}

