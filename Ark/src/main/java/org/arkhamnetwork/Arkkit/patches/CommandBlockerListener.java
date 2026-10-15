/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

final class CommandBlockerListener
        implements Listener {
    CommandBlockerListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerComandPreProcess(PlayerCommandPreprocessEvent e) {
        boolean op = e.getPlayer().isOp();
        boolean banteam = e.getPlayer().hasPermission("essentials.god");
        boolean blocked = false;
        String cmd = e.getMessage().replace("/", "");
        if (cmd.contains(" ")) {
            String currentCommandString = "";
            for (String s : cmd.split(" ")) {
                if (CommandBlocker.isBlockedCommand(currentCommandString = currentCommandString + s)) {
                    blocked = true;
                    break;
                }
                currentCommandString = currentCommandString + " ";
            }
        }
        if (blocked || CommandBlocker.isBlockedCommand(cmd)) {
            if (!op) {
                if (cmd.equals("fly") && banteam) {
                    return;
                }
                e.setCancelled(true);
                e.setMessage("/null");
                e.getPlayer().sendMessage(ChatColor.RED + "The " + ChatColor.UNDERLINE + "/" + cmd + ChatColor.RED + " command is currently disabled.");
            } else {
                e.getPlayer().sendMessage(ChatColor.RED + "'" + cmd + "' is a blocked command, but since you're an OP it has been executed.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreProcessUnique(PlayerCommandPreprocessEvent e) {
        String cmd;
        boolean op = e.getPlayer().isOp();
        if (!op && (cmd = e.getMessage().toLowerCase()).startsWith("/f money ff")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot transfer money directly from one faction to another on this server.");
            return;
        }
    }
}

