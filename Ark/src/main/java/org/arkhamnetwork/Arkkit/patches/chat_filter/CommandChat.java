/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches.chat_filter;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

final class CommandChat
        extends AbstractCommand {
    public CommandChat(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player;
        Player player2 = player = sender instanceof Player ? (Player) sender : null;
        if (player != null && !player.hasPermission("simplechat.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /chat <clear|toggle|reload>");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                ChatUtils.clearChat(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                ChatUtils.toggleChat(player);
                return true;
            }
            sender.sendMessage(ChatColor.RED + " Usage: /chat <clear|toggle|reload>");
            return true;
        }
        return false;
    }
}

