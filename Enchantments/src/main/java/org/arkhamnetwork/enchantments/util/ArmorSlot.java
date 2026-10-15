package org.arkhamnetwork.enchantments.util;

import org.bukkit.inventory.ItemStack;

public enum ArmorSlot {
    HELMET(3),
    CHESTPLATE(2),
    LEGGINGS(1),
    BOOTS(0);

    private int index;

    private ArmorSlot(final int index) {
        this.index = index;
    }

    public static ArmorSlot getArmorSlotByIndex(final int i) {
        for (final ArmorSlot as : values()) {
            if (as.index == i) {
                return as;
            }
        }
        return null;
    }

    public static ArmorSlot getArmorType(final ItemStack i) {
        final String name = i.getType().name();
        if (name.contains("HELMET")) {
            return ArmorSlot.HELMET;
        }
        if (name.contains("CHEST")) {
            return ArmorSlot.CHESTPLATE;
        }
        if (name.contains("LEG")) {
            return ArmorSlot.LEGGINGS;
        }
        if (name.contains("BOOTS")) {
            return ArmorSlot.BOOTS;
        }
        return null;
    }

    public int getIndex() {
        return this.index;
    }
}
