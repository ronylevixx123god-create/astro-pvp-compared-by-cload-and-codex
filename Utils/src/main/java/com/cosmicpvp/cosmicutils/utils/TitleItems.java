/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TitleItems {
    public static ItemStack getTitleItem(String title) {
        return new ItemBuilder(Material.PAPER).name(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "TITLE \"" + (Object) ((Object) ChatColor.WHITE) + title + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "\"").lore((Object) ((Object) ChatColor.GRAY) + "Right-click this item to unlock ", (Object) ((Object) ChatColor.GRAY) + "the " + title + " title on this planet.").build();
    }

    public static String getTitleFromItemstack(ItemStack i) {
        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().startsWith(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "TITLE ")) {
            return ChatColor.stripColor(i.getItemMeta().getDisplayName().split(" ")[1].replace("\"", ""));
        }
        return null;
    }
}

