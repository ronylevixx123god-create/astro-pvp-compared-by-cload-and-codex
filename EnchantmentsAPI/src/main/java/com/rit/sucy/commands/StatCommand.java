/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.rit.sucy.commands;

import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.commands.StatTask;
import com.rit.sucy.service.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StatCommand
implements ICommand {
    @Override
    public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                Material mat = Material.getMaterial((String)args[0].toUpperCase());
                if (mat == null) {
                    mat = Material.getMaterial((int)Integer.parseInt(args[0]));
                }
                if (mat == null) {
                    return false;
                }
                ItemStack item = new ItemStack(mat);
                if (EnchantmentAPI.getAllValidEnchants(item).size() == 0) {
                    sender.sendMessage((Object)ChatColor.DARK_RED + "That item has no natural enchantments");
                    return true;
                }
                int level = Integer.parseInt(args[1]);
                plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)plugin, (BukkitRunnable)new StatTask(sender, item, level));
                return true;
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }
}

