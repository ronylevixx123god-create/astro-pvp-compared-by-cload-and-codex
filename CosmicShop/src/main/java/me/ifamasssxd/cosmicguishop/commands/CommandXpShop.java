/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicguishop.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import me.ifamasssxd.cosmicguishop.menus.XpShopMenu;
import me.ifamasssxd.cosmicguishop.utils.Base64Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandXpShop
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (!player.isOp() || args.length == 0) {
            new XpShopMenu(player).getExpShopUI().open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("additem") && args.length == 2) {
            ItemStack item = player.getItemInHand();
            if (item == null || item.getType() == Material.AIR) {
                player.sendMessage(CC.Red + "You must be holding an item in your hand");
                return true;
            }
            List<String> list = CosmicGUIShop.getInstance().getItemList();
            for (String string : list) {
                ItemStack stack = Base64Utils.getItem(string);
                if (!item.equals(stack)) continue;
                player.sendMessage(CC.Red + "This item is already in the xpshop");
                return true;
            }
            list.add(Base64Utils.toBase64(new ItemStack[]{item}, 9));
            String i = list.get(list.size() - 1);
            int cost = 0;
            try {
                cost = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                cost = 10;
            }
            i = i + ":" + cost;
            list.set(list.size() - 1, i);
            CosmicGUIShop.getInstance().getConfig().set("items", list);
            CosmicGUIShop.getInstance().saveConfig();
            CosmicGUIShop.getInstance().loadConfig();
            CosmicGUIShop.getInstance().setItemList(list);
            player.sendMessage(CC.Green + "Added item " + item.getAmount() + "x " + item.getItemMeta().getDisplayName() + CC.Green + " costing " + cost + "xp");
        } else if (args[0].equalsIgnoreCase("removeitem") && args.length == 1) {
            int i;
            ItemStack item = player.getItemInHand();
            if (item == null || item.getType() == Material.AIR) {
                player.sendMessage(CC.Red + "You must be holding an item in your hand");
                return true;
            }
            List<String> list = CosmicGUIShop.getInstance().getItemList();
            ItemStack[] items = new ItemStack[list.size()];
            for (i = 0; i < list.size(); ++i) {
                String l = list.get(i);
                items[i] = Base64Utils.getItem(l);
            }
            for (i = list.size() - 1; i >= 0; --i) {
                ItemStack x = items[i];
                if (!x.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) continue;
                list.remove(i);
                CosmicGUIShop.getInstance().getConfig().set("items", list);
                CosmicGUIShop.getInstance().saveConfig();
                CosmicGUIShop.getInstance().loadConfig();
                CosmicGUIShop.getInstance().setItemList(list);
                player.sendMessage(CC.Green + "Removed item + " + item.getItemMeta().getDisplayName() + CC.Green + " from the xp shop!");
                return true;
            }
        }
        return false;
    }
}

