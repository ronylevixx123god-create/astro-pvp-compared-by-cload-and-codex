/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicmysterychest.commands;

import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLootboxToggle
        implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;
        boolean toggle = CosmicMysteryChest.get().getShardAnimations().anotoggle(player);
        ChatColor color = toggle ? ChatColor.RED : ChatColor.GREEN;
        player.sendMessage(color + ChatColor.BOLD.toString() + "(!)" + color + " You toggled Lootbox Animations " + (toggle ? color + ChatColor.BOLD.toString() + "OFF" : color + ChatColor.BOLD.toString() + "ON"));
        return false;
    }
}

