/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.patches.Easter
 *  org.arkhamnetwork.CosmicCustomTNT.me.astropvp.cratekeys.enums.TNTTypes
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.cmds;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.listeners.EnvoySummonItemListener;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGiveDp
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givedp")) {
            String p_name = null;
            if (sender instanceof Player && !((Player)sender).isOp()) {
                return true;
            }
            String string = args.length == 2 ? args[1] : (p_name = sender instanceof Player ? ((Player)sender).getName() : null);
            if (p_name == null) {
                CosmicSpaceChestsPlugin.log.debug("/givedp [1-6/envoy] <player>", this.getClass());
                return true;
            }
            if (args.length < 1) {
                ((Player)sender).sendMessage((Object)ChatColor.RED + "/givedp [1-6/envoy] <player>");
                return true;
            }
            if (Bukkit.getPlayer((String)p_name) == null) {
                ((Player)sender).sendMessage(ChatColor.RED + p_name + " cannot be found.");
                return true;
            }
            Player ply = Bukkit.getPlayer((String)p_name);
            if (args[0].equalsIgnoreCase("envoy")) {
                ItemStack is = EnvoySummonItemListener.buildEnvoySpawnItem();
                ItemUtils.giveItemToPlayer(ply, is);
                ply.sendMessage((Object)ChatColor.GRAY + "You got a " + (Object)ChatColor.GOLD + (Object)ChatColor.UNDERLINE + "Cosmic Envoy Spawner!");
                ply.updateInventory();
                if (!sender.getName().equalsIgnoreCase(ply.getName())) {
                    sender.sendMessage(ChatColor.GRAY + p_name + " has been given 1x " + ChatColor.GOLD + ChatColor.UNDERLINE + "Cosmic Envoy Spawner!");
                } else {
                    CosmicSpaceChestsPlugin.log.debug(p_name + " has been given 1x " + ChatColor.GOLD.toString() + (Object)ChatColor.UNDERLINE + "Cosmic Envoy Spawner!", this.getClass());
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("easter")) {
                ply.sendMessage((Object)ChatColor.GRAY + "You got an " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "Easter Egg!");
                ply.updateInventory();
                if (sender instanceof Player && !((Player)sender).getName().equalsIgnoreCase(ply.getName())) {
                    ((Player)sender).sendMessage((Object)ChatColor.GRAY + p_name + " has been given 1x " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "Easter Egg!");
                } else {
                    CosmicSpaceChestsPlugin.log.debug(p_name + " has been given 1x " + ChatColor.YELLOW.toString() + (Object)ChatColor.UNDERLINE + "Easter Egg!", this.getClass());
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("whitescroll")) {
                int amount = 1;
                if (args.length == 3) {
                    amount = Integer.parseInt(args[2]);
                }
                ItemStack is = EnchantmentBook.getWhiteScroll();
                for (int x = amount; x > 0; --x) {
                    ItemUtils.giveItemToPlayer(ply, is);
                    ply.sendMessage((Object)ChatColor.GRAY + "You got a " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "White Scroll!");
                }
                ply.updateInventory();
                if (sender instanceof Player && !((Player)sender).getName().equalsIgnoreCase(ply.getName())) {
                    ((Player)sender).sendMessage((Object)ChatColor.GRAY + p_name + " has been given 1x " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "White Scroll!");
                } else {
                    CosmicSpaceChestsPlugin.log.debug(p_name + " has been given 1x " + ChatColor.YELLOW.toString() + (Object)ChatColor.UNDERLINE + "White Scroll!", this.getClass());
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("blackscroll")) {
                int amount = 1;
                if (args.length == 3) {
                    amount = Integer.parseInt(args[2]);
                }
                ItemStack is = EnchantmentBook.getBlackScroll();
                for (int x = amount; x > 0; --x) {
                    ItemUtils.giveItemToPlayer(ply, is);
                    ply.sendMessage((Object)ChatColor.GRAY + "You got a " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "Black Scroll!");
                }
                ply.updateInventory();
                if (sender instanceof Player && !((Player)sender).getName().equalsIgnoreCase(ply.getName())) {
                    ((Player)sender).sendMessage((Object)ChatColor.GRAY + p_name + " has been given " + amount + "x " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + "Black Scroll!");
                } else {
                    CosmicSpaceChestsPlugin.log.debug(p_name + " has been given " + amount + "x " + ChatColor.YELLOW.toString() + (Object)ChatColor.UNDERLINE + "Black Scroll!", this.getClass());
                }
                return true;
            }

            ItemRarity ir = CosmicSpaceChestsAPI.getItemRarity(Integer.parseInt(args[0]));
            ChatColor cc = CosmicSpaceChestsAPI.getColor(ir);
            ItemStack is = CosmicSpaceChestsAPI.getDropPackageChest(ir);
            ItemUtils.giveItemToPlayer(ply, is);
            ply.sendMessage((Object)cc + "" + (Object)ChatColor.BOLD + "+ 1x " + (Object)cc + (Object)ChatColor.UNDERLINE + ir.name + " Cosmic Chest!");
            ply.updateInventory();
            if (sender instanceof Player && !((Player)sender).getName().equalsIgnoreCase(ply.getName())) {
                ((Player)sender).sendMessage((Object)ChatColor.GRAY + p_name + " has been given 1x " + (Object)cc + (Object)ChatColor.UNDERLINE + ir.name + " Cosmic Chest!");
            } else {
                CosmicSpaceChestsPlugin.log.debug(p_name + " has been given 1x " + (Object)cc + (Object)ChatColor.UNDERLINE + ir.name + " Cosmic Chest!", this.getClass());
            }
        }
        return true;
    }
}

