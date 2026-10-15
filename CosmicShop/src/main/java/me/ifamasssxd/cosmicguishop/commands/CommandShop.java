/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.Validate
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicguishop.commands;

import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import me.ifamasssxd.cosmicguishop.struct.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class CommandShop
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.isOp()) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    try {
                        CosmicGUIShop.get().getShopManager().loadAllShopItems();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(ChatColor.RED + "Shop reloaded");
                    return true;
                }
                if (args[0].equalsIgnoreCase("setprice")) {
                    sender.sendMessage(ChatColor.RED + "Usage: /setprice <CATEGORY> <ITEM> <PRICE> <sellPrice>");
                    sender.sendMessage(ChatColor.RED + "EXAMPLE: /setprice BUILDING_BLOCKS SMOOTH_BRICK@4 1.50 0");
                    sender.sendMessage(ChatColor.RED + "Use: '/setprice BUILDING_BLOCKS SMOOTH_BRICK@4 -1' to remove the item from the shop.");
                    return true;
                }
            } else if ((args.length == 4 || args.length == 5) && args[0].equalsIgnoreCase("setprice")) {
                double sellPrice;
                double price;
                ShopManager manager = CosmicGUIShop.get().getShopManager();
                String catName = args[1];
                String itemString = args[2];
                try {
                    price = Double.parseDouble(args[3]);
                    sellPrice = args.length == 5 ? Double.parseDouble(args[4]) : 0.0;
                    Validate.isTrue((price >= -1.0 && price < 1.0E9 ? 1 : 0) != 0);
                    Validate.isTrue((sellPrice >= 0.0 && sellPrice < 1.0E9 ? 1 : 0) != 0);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Please enter a valid price!");
                    return true;
                }
                ShopMaterialData data = manager.getShopMaterialFromString(itemString.replace(":", "@"));
                if (data == null) {
                    sender.sendMessage(ChatColor.RED + "Unable to load item from " + itemString);
                    return true;
                }
                ShopCategory cat = ShopCategory.getFromName(catName);
                if (cat == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid category. Valid Categories:");
                    Arrays.stream(ShopCategory.values()).forEach(real -> sender.sendMessage(ChatColor.RED + real.name()));
                    return true;
                }
                ShopItemMap shopItems = manager.getShopItems().computeIfAbsent(cat, map -> new ShopItemMap());
                ItemPrice currentPrice = shopItems.get(data);
                if (currentPrice != null) {
                    sender.sendMessage(ChatColor.RED + "Overriding prices, BUY: " + currentPrice.getBuyPrice() + " /ea SELL: " + currentPrice.getSellPrice() + " /ea");
                    if (price == -1.0) {
                        shopItems.remove(data);
                        sender.sendMessage(ChatColor.RED + "Item removed from shop.");
                    }
                    shopItems.remove(data);
                } else if (price == -1.0) {
                    shopItems.remove(data);
                    sender.sendMessage(ChatColor.RED + "Item not registered.");
                    Bukkit.getLogger().info("Keys: " + shopItems.keySet());
                    return true;
                }
                if (price != -1.0) {
                    shopItems.put(data, new ItemPrice(price, sellPrice));
                    sender.sendMessage(ChatColor.RED + "Added to prices config!");
                }
                itemString = data.toString();
                if (price == -1.0 && sellPrice <= 0.0) {
                    manager.getPriceConfig().set("prices." + cat.name() + "." + itemString, null);
                } else {
                    manager.getPriceConfig().set("prices." + cat.name() + "." + itemString + ".buy", price);
                    manager.getPriceConfig().set("prices." + cat.name() + "." + itemString + ".sell", sellPrice);
                }
                try {
                    manager.saveFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        CosmicGUIShop.get().getShopManager().getShopMenu(player).open(player);
        return false;
    }
}

