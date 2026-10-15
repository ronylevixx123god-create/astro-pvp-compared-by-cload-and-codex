/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.titles.utils;

import net.astropvp.core.titles.PlayerTitleAPI;

import java.util.ArrayList;

import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TitleInterface {
    public static void openInterface(Player player, int page) {
        ArrayList<String> playertitlearray = PlayerTitleAPI.getUnlockedTitles(player.getUniqueId());
        int titlecount = playertitlearray.size();
        int invSize = 9;
        invSize = titlecount % 9 == 0 ? titlecount : (titlecount / 9 + 1) * 9;
        if (invSize < 9 || titlecount <= 0) {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + (Object)ChatColor.RED + " You have not yet unlocked any titles!");
            player.sendMessage((Object)ChatColor.GRAY + "Titles may be gained through accomplishing achievements in-game or purchasing them with /buy!");
            return;
        }
        if (invSize > 54) {
            invSize = 54;
        }
        String name = ChatColor.BLACK.toString() + "Unlocked Player Titles";
        int perPage = 53;
        int pages = (int)Math.max(1.0, Math.ceil((double)titlecount / (double)perPage));
        if (pages > 2) {
            perPage = 52;
            pages = (int)Math.max(1.0, Math.ceil((double)titlecount / (double)perPage));
        }
        page = Math.min(Math.max(page, 1), pages);
        if (pages > 1 && (name = name + " (" + page + "/" + pages + ")").length() > 32) {
            name = name.substring(0, 32);
        }
        Inventory inventory = Bukkit.createInventory(null, (int)invSize, (String)name);
        String equippedTitle = TitleUtil.removeColorCodes(AstroCore.getInstance().getDataManager().getEquippedTitle(player.getUniqueId()));
        int startIndex = page * perPage - perPage;
        int endIndex = startIndex + perPage;
        for (int index = startIndex; index < endIndex && index < playertitlearray.size(); ++index) {
            String t = playertitlearray.get(index);
            if (equippedTitle.equalsIgnoreCase(t)) {
                inventory.addItem(new ItemStack[]{ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)5, ChatColor.GOLD.toString() + (Object)ChatColor.BOLD + t.replace("_", " "), (Object)ChatColor.GOLD + "Equipped", (Object)ChatColor.GRAY + "Click to un-equip this title.")});
                continue;
            }
            inventory.addItem(new ItemStack[]{ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)0, ChatColor.WHITE.toString() + (Object)ChatColor.BOLD + t.replace("_", " "), (Object)ChatColor.WHITE + "Unlocked", (Object)ChatColor.GRAY + "Click to equip this title.")});
        }
        if (pages > 1) {
            if (page > 1) {
                inventory.setItem(inventory.getSize() - 9, ItemUtils.buildItem(Material.ARROW, (short)0, (Object)ChatColor.RED + ChatColor.BOLD.toString() + "Previous Page", (Object)ChatColor.GRAY + "Click to view previous page"));
            }
            if (page < pages) {
                inventory.setItem(inventory.getSize() - 1, ItemUtils.buildItem(Material.ARROW, (short)0, (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "Next Page", (Object)ChatColor.GRAY + "Click to view next page"));
            }
        }
        for (int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) continue;
            inventory.setItem(i, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)7, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "???", (Object)ChatColor.GRAY + "Unlock new titles by opening", (Object)ChatColor.GRAY + "space chests, defeating bosses,", (Object)ChatColor.GRAY + "or at " + (Object)ChatColor.GOLD + (Object)ChatColor.UNDERLINE + "buy.astropvp.net"));
        }
        player.openInventory(inventory);
    }
}

