/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.compilex.CosmicCustomCreepers.utils;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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

    public static ItemStack getMobSpawner(EntityType entityType) {
        return ItemUtils.buildItem(Material.MOB_SPAWNER, entityType.getTypeId(), ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + ItemUtils.getEntityName(entityType) + ChatColor.GOLD.toString() + " Spawner", (Object)ChatColor.GRAY + "Be careful where you place this!");
    }

    private static String getEntityName(EntityType et) {
        if (et == EntityType.IRON_GOLEM) {
            return "Iron Golem";
        }
        if (et == EntityType.MAGMA_CUBE) {
            return "Magma Cube";
        }
        return et.getName();
    }
}

