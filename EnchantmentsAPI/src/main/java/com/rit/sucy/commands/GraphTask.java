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
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GraphTask
extends BukkitRunnable {
    private static final DecimalFormat format = new DecimalFormat("##0.0");
    private final CommandSender sender;
    private final ItemStack item;
    private final CustomEnchantment enchant;
    private final int maxEnchants;

    public GraphTask(CommandSender sender, ItemStack item, CustomEnchantment enchant) {
        this.sender = sender;
        this.item = item;
        this.enchant = enchant;
        this.maxEnchants = ((EnchantmentAPI)Bukkit.getPluginManager().getPlugin("EnchantmentAPI")).getModuleForClass(RootConfig.class).getInt(RootNode.MAX_ENCHANTS);
    }

    public void run() {
        int i;
        Hashtable<String, int[]> points = new Hashtable<String, int[]>();
        for (int i2 = 1; i2 <= 30; ++i2) {
            points.put(this.enchant.name() + i2, new int[this.enchant.getMaxLevel()]);
        }
        for (int j = 1; j <= 30; ++j) {
            for (i = 0; i < 100000; ++i) {
                Map<CustomEnchantment, Integer> list = EEnchantTable.enchant(null, this.item, j, this.maxEnchants, false).getAddedEnchants();
                for (Map.Entry<CustomEnchantment, Integer> entry : list.entrySet()) {
                    if (!entry.getKey().equals(this.enchant)) continue;
                    int[] values = (int[])points.get(this.enchant.name() + j);
                    int n = entry.getValue() - 1;
                    values[n] = values[n] + 1;
                }
            }
        }
        int max = 0;
        for (int j = 1; j <= 30; ++j) {
            int[] data = (int[])points.get(this.enchant.name() + j);
            if (data == null) continue;
            for (int k = 0; k < this.enchant.getMaxLevel(); ++k) {
                if (data[k] <= max) continue;
                max = data[k];
            }
        }
        max = (max + 999) / 1000;
        for (i = 9; i >= 0; --i) {
            ChatColor lc = i == 0 ? ChatColor.GRAY : ChatColor.DARK_GRAY;
            String line = (Object)ChatColor.GOLD + "" + format.format((double)(i * max) / 10.0) + "-" + format.format((double)((i + 1) * max) / 10.0) + "%" + (Object)lc + "_";
            while (line.length() < 16) {
                line = line + "_";
            }
            line = line + (Object)ChatColor.GRAY + "|";
            for (int j = 1; j <= 30; ++j) {
                int[] data = (int[])points.get(this.enchant.name() + j);
                String piece = (Object)lc + "_";
                for (int k = 0; k < this.enchant.getMaxLevel(); ++k) {
                    if (data[k] <= i * max * 100 || data[k] > (i + 1) * max * 100) continue;
                    piece = (Object)ChatColor.getByChar((char)((char)(49 + k % 6))) + "X";
                }
                line = line + piece;
            }
            this.sender.sendMessage(line);
        }
        this.sender.sendMessage((Object)ChatColor.DARK_GRAY + "||__________" + (Object)ChatColor.GRAY + "|" + (Object)ChatColor.DARK_GRAY + "____" + (Object)ChatColor.GRAY + "5" + (Object)ChatColor.DARK_GRAY + "____" + (Object)ChatColor.GRAY + "10" + (Object)ChatColor.DARK_GRAY + "___" + (Object)ChatColor.GRAY + "15" + (Object)ChatColor.DARK_GRAY + "___" + (Object)ChatColor.GRAY + "20" + (Object)ChatColor.DARK_GRAY + "___" + (Object)ChatColor.GRAY + "25" + (Object)ChatColor.DARK_GRAY + "___" + (Object)ChatColor.GRAY + "30");
    }
}

