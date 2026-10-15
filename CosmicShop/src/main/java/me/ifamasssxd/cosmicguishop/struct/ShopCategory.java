/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 */
package me.ifamasssxd.cosmicguishop.struct;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum ShopCategory {
    POTION(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Potion Shop", new ShopMaterialData(Material.POTION)),
    REDSTONE(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Raid Shop", new ShopMaterialData(Material.REDSTONE)),
    SPAWNERS(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Spawners", new ShopMaterialData(Material.MOB_SPAWNER)),
    BUILDING_BLOCKS(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Building Blocks", new ShopMaterialData(Material.SMOOTH_BRICK)),
    ORES(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Ores and Gems", new ShopMaterialData(Material.EMERALD)),
    FOOD_AND_FARMING(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Food and Farming", new ShopMaterialData(Material.COOKED_BEEF)),
    MOB_DROPS(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Mob Drops", new ShopMaterialData(Material.BLAZE_ROD)),
    SPECIALTY(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Speciality", new ShopMaterialData(Material.EYE_OF_ENDER)),
    BREWING(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Brewing", new ShopMaterialData(Material.BREWING_STAND_ITEM)),
    COLORED_WOOL(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Wool", new ShopMaterialData(Material.WOOL)),
    COLORED_GLASS(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Glass", new ShopMaterialData(Material.STAINED_GLASS, DyeColor.GREEN.getWoolData())),
    BASE_GRIND(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Base Grind", new ShopMaterialData(Material.OBSIDIAN)),
    CLAY(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Clay", new ShopMaterialData(Material.STAINED_CLAY)),
    FLOWERS(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Flowers", new ShopMaterialData(Material.RED_ROSE));

    private final String displayName;
    private final ShopMaterialData displayData;

    ShopCategory(String displayName, ShopMaterialData displayData) {
        this.displayName = displayName;
        this.displayData = displayData;
    }

    public static ShopCategory getFromName(String name) {
        for (ShopCategory values : ShopCategory.values()) {
            if (!values.name().equals(name)) continue;
            return values;
        }
        return null;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ShopMaterialData getDisplayData() {
        return this.displayData;
    }
}

