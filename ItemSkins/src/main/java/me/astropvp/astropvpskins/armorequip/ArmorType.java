package me.astropvp.astropvpskins.armorequip;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public enum ArmorType
{
    HELMET(5),
    CHESTPLATE(6),
    LEGGINGS(7),
    BOOTS(8);

    private final int slot;

    private ArmorType(final int slot) {
        this.slot = slot;
    }

    public static final ArmorType matchType(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return null;
        }
        final String type = itemStack.getType().name();
        if (type.endsWith("_HELMET") || type.startsWith("SKULL")) {
            return ArmorType.HELMET;
        }
        if (type.endsWith("_CHESTPLATE")) {
            return ArmorType.CHESTPLATE;
        }
        if (type.endsWith("_LEGGINGS")) {
            return ArmorType.LEGGINGS;
        }
        if (type.endsWith("_BOOTS")) {
            return ArmorType.BOOTS;
        }
        return null;
    }

    public int getSlot() {
        return this.slot;
    }
}
