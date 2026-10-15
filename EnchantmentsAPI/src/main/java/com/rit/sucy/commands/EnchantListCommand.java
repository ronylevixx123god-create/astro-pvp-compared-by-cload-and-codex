/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.rit.sucy.commands;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.enchanting.VanillaEnchantment;
import com.rit.sucy.service.ICommand;
import com.rit.sucy.service.PermissionNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EnchantListCommand
implements ICommand {
    @Override
    public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(PermissionNode.LIST.getNode())) {
            if (args.length > 0) {
                try {
                    int page = Integer.parseInt(args[0]);
                    if (EnchantmentAPI.getEnchantments().size() > 0) {
                        ArrayList<CustomEnchantment> enchants = new ArrayList<CustomEnchantment>(EnchantmentAPI.getEnchantments());
                        Collections.sort(enchants);
                        Iterator<CustomEnchantment> iterator = enchants.iterator();
                        while (iterator.hasNext()) {
                            CustomEnchantment next = iterator.next();
                            if (!(next instanceof VanillaEnchantment)) continue;
                            iterator.remove();
                        }
                        if (page * 9 > enchants.size() + 8) {
                            page = (enchants.size() + 8) / 9;
                        } else if (page < 1) {
                            page = 1;
                        }
                        sender.sendMessage((Object)ChatColor.DARK_GREEN + "Enchantment Details - Page " + page + " / " + (enchants.size() + 8) / 9);
                        for (int i = (page - 1) * 9; i < page * 9 && i < enchants.size(); ++i) {
                            sender.sendMessage((Object)ChatColor.GOLD + enchants.get(i).name() + (Object)ChatColor.GRAY + " - " + (enchants.get(i).getDescription() == null ? "No description" : enchants.get(i).getDescription()));
                        }
                    } else {
                        sender.sendMessage((Object)ChatColor.DARK_RED + "There are no registered enchantments to display");
                    }
                    return true;
                }
                catch (NumberFormatException page) {
                    // empty catch block
                }
            }
            String message = " Registered enchantments: ";
            int count = 0;
            if (EnchantmentAPI.getEnchantments().size() > 0) {
                ArrayList<CustomEnchantment> enchants = new ArrayList<CustomEnchantment>(EnchantmentAPI.getEnchantments());
                Collections.sort(enchants);
                for (CustomEnchantment enchantment : enchants) {
                    if (enchantment instanceof VanillaEnchantment) continue;
                    message = message + (Object)ChatColor.GOLD + enchantment.name() + (Object)ChatColor.GRAY + ", ";
                    ++count;
                }
                message = message.substring(0, message.length() - 2);
            }
            sender.sendMessage((Object)ChatColor.GREEN + "" + count + message);
            return true;
        }
        return false;
    }
}

