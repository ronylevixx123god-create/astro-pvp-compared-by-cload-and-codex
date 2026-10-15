/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

final class CommandTogglePlayer
        extends AbstractCommand {
    protected static List<Player> hidden_players;

    public CommandTogglePlayer(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
        hidden_players = new ArrayList<Player>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            return true;
        }
        Player pl = (Player) sender;
        if (hidden_players.contains(pl)) {
            for (Player ply : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(ply);
                ply.showPlayer(pl);
            }
            hidden_players.remove(pl);
            pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "BAM!");
        } else {
            for (Player ply : Bukkit.getOnlinePlayers()) {
                if (ply.isOp()) continue;
                pl.hidePlayer(ply);
                ply.hidePlayer(pl);
            }
            hidden_players.add(pl);
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "POOF!");
        }
        return true;
    }
}

