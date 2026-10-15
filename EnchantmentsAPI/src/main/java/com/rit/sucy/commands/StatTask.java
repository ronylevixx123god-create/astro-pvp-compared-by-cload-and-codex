/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.rit.sucy.commands;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.config.RootConfig;
import com.rit.sucy.config.RootNode;
import com.rit.sucy.enchanting.EEnchantTable;
import com.rit.sucy.enchanting.VanillaEnchantment;
import com.rit.sucy.service.ENameParser;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class StatTask
extends BukkitRunnable {
    private static final DecimalFormat format = new DecimalFormat("##0.0#");
    private final ItemStack item;
    private final CommandSender sender;
    private final int level;
    private final int maxEnchants;

    public StatTask(CommandSender sender, ItemStack item, int level) {
        this.sender = sender;
        this.level = level;
        this.item = item;
        this.maxEnchants = ((EnchantmentAPI)Bukkit.getPluginManager().getPlugin("EnchantmentAPI")).getModuleForClass(RootConfig.class).getInt(RootNode.MAX_ENCHANTS);
    }

    public void run() {
        List<CustomEnchantment> validEnchants = EnchantmentAPI.getAllValidEnchants(this.item);
        Hashtable<String, int[]> data = new Hashtable<String, int[]>();
        for (int i = 0; i < 100000; ++i) {
            Map<CustomEnchantment, Integer> list = EEnchantTable.enchant(null, this.item, this.level, this.maxEnchants, false).getAddedEnchants();
            for (Map.Entry<CustomEnchantment, Integer> entry : list.entrySet()) {
                String name = entry.getKey().name();
                if (!data.containsKey(name)) {
                    data.put(name, new int[entry.getKey().getMaxLevel()]);
                }
                int[] values = (int[])data.get(name);
                int n = entry.getValue() - 1;
                values[n] = values[n] + 1;
            }
        }
        this.sender.sendMessage((Object)ChatColor.GOLD + this.item.getType().name() + (Object)ChatColor.DARK_GREEN + " - Enchantment Stats (Lv " + this.level + ")");
        for (CustomEnchantment enchant : validEnchants) {
            if (enchant.getMaxLevel() == 0) continue;
            String message = enchant.name() + " (";
            if (enchant instanceof VanillaEnchantment) {
                message = ENameParser.getVanillaName(((VanillaEnchantment)enchant).getVanillaEnchant()) + " (";
            }
            if (data.containsKey(enchant.name())) {
                int[] values = (int[])data.get(enchant.name());
                boolean index = false;
                for (int i = 0; i < enchant.getMaxLevel(); ++i) {
                    message = message + (Object)ChatColor.GOLD + format.format((double)values[i] / 1000.0) + "%" + (Object)ChatColor.DARK_GREEN + ", ";
                }
            } else {
                for (int i = 1; i <= enchant.getMaxLevel(); ++i) {
                    message = message + (Object)ChatColor.GOLD + "0.0%" + (Object)ChatColor.DARK_GREEN + ", ";
                }
            }
            this.sender.sendMessage((Object)ChatColor.DARK_GREEN + message.substring(0, message.length() - 2) + ")");
        }
    }
}

