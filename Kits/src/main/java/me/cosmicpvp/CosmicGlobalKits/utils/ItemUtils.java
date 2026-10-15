/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.cosmicpvp.CosmicGlobalKits.utils;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public static ItemStack buildItem(Material m, short metaData, String name, String ... description) {
        ItemStack is = new ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null) {
            im.setLore(Arrays.asList(description));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setAmount(ItemStack i, int x) {
        i.setAmount(x);
        return i;
    }

    public static void giveItemToPlayer(Player p, ItemStack i) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(new ItemStack[]{i});
        } else {
            Bukkit.getLogger().info("[CosmicGlobalKits] " + p.getName() + " did not have inventory space for " + (Object)i + ", dropping on ground!");
            p.getWorld().dropItem(p.getLocation(), i);
        }
    }
}

