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

class CommandChatFilter
        extends AbstractCommand {
    public CommandChatFilter(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player pl = (Player) sender;
        if (ChatFilter.disabledFilteredPlayers.add(pl.getUniqueId())) {
            pl.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "(!) " + ChatColor.AQUA + "Chat Filter " + ChatColor.RED + ChatColor.BOLD.toString() + "DISABLED");
            pl.sendMessage(ChatColor.GRAY + "Global chat and /msg's will no longer be filtered for profanity.");
        } else {
            pl.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "(!) " + ChatColor.AQUA + "Chat Filter " + ChatColor.GREEN + ChatColor.BOLD.toString() + "ENABLED");
            pl.sendMessage(ChatColor.GRAY + "Global chat and /msg's will be filtered for profanity.");
            ChatFilter.disabledFilteredPlayers.remove(pl.getUniqueId());
        }
        return false;
    }
}

