/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.rit.sucy.commands;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.ICommand;
import com.rit.sucy.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddEnchantCommand
implements ICommand {
    @Override
    public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission(PermissionNode.ADMIN.getNode())) {
            String name = args[0];
            int difference = 0;
            int level = 1;
            try {
                level = Integer.parseInt(args[args.length - 1]);
                difference = 1;
            }
            catch (Exception exception) {
                // empty catch block
            }
            for (int i = 1; i < args.length - difference; ++i) {
                name = name + " " + args[i];
            }
            name = ENameParser.getBukkitName(name);
            Player player = (Player)sender;
            CustomEnchantment enchantment = EnchantmentAPI.getEnchantment(name);
            if (enchantment == null) {
                sender.sendMessage((Object)ChatColor.DARK_RED + name + " is not a registered enchantment!");
            } else {
                player.setItemInHand(enchantment.addToItem(player.getItemInHand(), level));
                player.sendMessage((Object)ChatColor.GREEN + "Enchantment has been applied.");
            }
        }
        return true;
    }
}

