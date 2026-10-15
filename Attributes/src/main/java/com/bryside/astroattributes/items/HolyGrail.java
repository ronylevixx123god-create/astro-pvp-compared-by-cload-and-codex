package com.bryside.astroattributes.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class HolyGrail {
    public static Material material;
    public static String displayName;

    static {
        material = Material.SAPLING;
        displayName = CC.GoldB + "Holy Grail";
    }

    public static ItemStack createNewHolyGrail() {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        List lore = Arrays.asList(CC.Yellow + "A rare consumable designed for", CC.Yellow + "the strongest of explorers.", CC.Yellow + "Used to allow multiple uses of the", CC.Yellow + "titan item (un-corrupts an item).", "", CC.GoldU + "REQ:" + CC.Yellow + " Titan Attribute applied to equipment.");
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean isHolyGrail(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName() && item.getType() == material) {
            ItemMeta itemMeta = item.getItemMeta();
            String display = itemMeta.getDisplayName();
            return display.contentEquals(displayName) && itemMeta.getLore().contains(CC.Yellow + "titan item (un-corrupts an item).");
        } else {
            return false;
        }
    }

    public static Material getMaterial() {
        return material;
    }
}
