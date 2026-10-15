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

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.commands.GraphTask;
import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GraphCommand
implements ICommand {
    private long timer = 0L;

    @Override
    public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            try {
                Material mat = Material.getMaterial((String)args[0].toUpperCase());
                if (mat == null) {
                    mat = Material.getMaterial((int)Integer.parseInt(args[0]));
                }
                if (mat == null) {
                    return false;
                }
                ItemStack item = new ItemStack(mat);
                String name = args[1];
                for (int i = 2; i < args.length; ++i) {
                    name = name + " " + args[i];
                }
                CustomEnchantment enchant = EnchantmentAPI.getEnchantment(ENameParser.getBukkitName(name));
                if (!enchant.canEnchantOnto(item)) {
                    sender.sendMessage((Object)ChatColor.DARK_RED + "That enchantment doesn't work on that item");
                    return true;
                }
                if (System.currentTimeMillis() - this.timer < 10000L) {
                    sender.sendMessage((Object)ChatColor.DARK_RED + "Please give the server a quick break!");
                    sender.sendMessage((Object)ChatColor.DARK_RED + "The command should only be used every 10 seconds!");
                    return true;
                }
                this.timer = System.currentTimeMillis();
                sender.sendMessage((Object)ChatColor.DARK_GREEN + "Calculating probabilities...");
                plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)plugin, (BukkitRunnable)new GraphTask(sender, item, enchant));
                return true;
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }
}

