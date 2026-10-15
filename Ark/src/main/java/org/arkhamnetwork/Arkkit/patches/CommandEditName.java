/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class CommandEditName
        extends AbstractCommand {
    public CommandEditName(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/editname <name>");
            return true;
        }
        String name = "";
        for (int i = 0; i < args.length; ++i) {
            name = name + ChatColor.translateAlternateColorCodes('&', i == args.length - 1 ? args[i] : args[i] + " ");
        }
        ItemStack i = ((Player) sender).getItemInHand();
        if (i == null) {
            sender.sendMessage(ChatColor.RED + "Please hold the item you wish to edit the name of!");
            return true;
        }
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        ((Player) sender).setItemInHand(i);
        sender.sendMessage(ChatColor.GREEN + "Name updated!");
        return true;
    }
}

