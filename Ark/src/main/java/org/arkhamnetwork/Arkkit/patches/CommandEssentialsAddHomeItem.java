/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

final class CommandEssentialsAddHomeItem
        extends AbstractCommand {
    public CommandEssentialsAddHomeItem(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "/ahi <player>");
            sender.sendMessage(ChatColor.GRAY + "Adds 1x HomeIncreaser item to specified <player>.");
            return true;
        }
        String pName = args[0];
        if (Bukkit.getPlayer(pName) != null) {
            Player p = Bukkit.getPlayer(pName);
            p.getInventory().addItem(EssentialsAddHomes.getAddHomeItem());
            sender.sendMessage(ChatColor.GRAY + "* Added 1x HomeIncreaser to " + p.getName() + "!");
        }
        return true;
    }
}

