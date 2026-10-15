/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.libs;

import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TitleItems {
    public static ItemStack getTitleItem(String title) {
        return ItemUtils.buildItem(Material.PAPER, (short)0, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "TITLE \"" + (Object)ChatColor.WHITE + title + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "\"", (Object)ChatColor.GRAY + "Right-click this item to unlock ", (Object)ChatColor.GRAY + "the " + title + " title on this planet.");
    }

    public static String getTitleFromItemstack(ItemStack i) {
        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().startsWith(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "TITLE ")) {
            return ChatColor.stripColor((String)i.getItemMeta().getDisplayName().split(" ")[1].replace("\"", ""));
        }
        return null;
    }
}

