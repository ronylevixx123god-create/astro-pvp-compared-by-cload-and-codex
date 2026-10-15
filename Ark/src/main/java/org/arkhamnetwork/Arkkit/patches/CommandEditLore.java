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

import java.util.ArrayList;
import java.util.List;

final class CommandEditLore
        extends AbstractCommand {
    public CommandEditLore(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List ilore;
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/editlore <add/remove> <lore>");
            return true;
        }
        String operation = args[0];
        String lore = "";
        for (int i = 1; i < args.length; ++i) {
            lore = lore + ChatColor.translateAlternateColorCodes('&', i == args.length - 1 ? args[i] : args[i] + " ");
        }
        ItemStack i = ((Player) sender).getItemInHand();
        if (i == null) {
            sender.sendMessage(ChatColor.RED + "Please hold the item you wish to edit the lore of!");
            return true;
        }
        ItemMeta im = i.getItemMeta();
        List list = ilore = im.hasLore() ? im.getLore() : new ArrayList();
        if (operation.equalsIgnoreCase("add")) {
            ilore.add(lore);
        } else if (operation.equalsIgnoreCase("remove")) {
            ilore.remove(lore);
        }
        im.setLore(ilore);
        i.setItemMeta(im);
        ((Player) sender).setItemInHand(i);
        sender.sendMessage(ChatColor.GREEN + "Lore updated!");
        return true;
    }
}

