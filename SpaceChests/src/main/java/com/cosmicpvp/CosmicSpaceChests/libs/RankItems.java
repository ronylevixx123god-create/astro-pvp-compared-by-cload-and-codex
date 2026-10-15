/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CompilexPermissions.structure.PermissionGroup
 *  org.apache.commons.lang.StringUtils
 *  org.arkhamnetwork.permissions.ArkhamPermissions
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.libs;

import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RankItems {
    public static HashMap<Integer, String> donorTiers = new HashMap();

    public static ItemStack getRankItem(String rank) {
        return ItemUtils.buildItem(Material.PAPER, (short)0, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "RANK \"" + (Object)ChatColor.WHITE + StringUtils.capitalize((String)rank) + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "\"", (Object)ChatColor.GRAY + "Right-click this item to unlock ", (Object)ChatColor.GRAY + "the " + StringUtils.capitalize((String)rank) + " Rank on this planet.");
    }

    public static String getRankFromItemstack(ItemStack i) {
        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "RANK ")) {
            return ChatColor.stripColor((String)i.getItemMeta().getDisplayName().split(" ")[1].replace("\"", "")).toLowerCase();
        }
        return null;
    }

    public static String getRank(int donorTier) {
        return donorTiers.containsKey(donorTier) ? donorTiers.get(donorTier) : null;
    }

    public static int getRankID(String rankName) {
        for (Map.Entry<Integer, String> data : donorTiers.entrySet()) {
            if (!data.getValue().equalsIgnoreCase(rankName)) continue;
            return data.getKey();
        }
        return -1;
    }
}

