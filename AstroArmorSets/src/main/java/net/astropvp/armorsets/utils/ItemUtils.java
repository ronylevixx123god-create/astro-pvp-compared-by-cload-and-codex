package net.astropvp.armorsets.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    public static boolean isWeapon(ItemStack item) {
        Material type = item.getType();
        String name = type.name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE") || name.equals("BOW");
    }
}
