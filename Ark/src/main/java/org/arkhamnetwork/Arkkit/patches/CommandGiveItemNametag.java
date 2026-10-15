/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandGiveItemNametag
        extends AbstractCommand {
    public CommandGiveItemNametag(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/giveitemtag <name>");
            return true;
        }
        String pname = args[0];
        Player pl = Bukkit.getPlayer(pname);
        if (pl == null) {
            sender.sendMessage(ChatColor.RED + "Could not locate player " + pname + "!");
            return true;
        }
        if (pl.getInventory().firstEmpty() != -1) {
            pl.getInventory().addItem(ItemNametag.getNametagItem());
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "You have recieved an Item Nametag!");
        } else {
            pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
            pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, an item nametag has been dropped at your feet.");
        }
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Gave 1x Item Nametag to " + pl.getName() + "!");
        return true;
    }
}

