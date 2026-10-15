package me.astropvp.astropvpskins.enums;

import org.bukkit.inventory.ItemStack;

public enum ItemSkinType {
    SWORD, AXE, BOW, WEAPON, HELMET, CHESTPLATE, LEGGINGS, BOOTS, AMULET, BELT, PICKAXE, TOOL, BACKPACK;

    public boolean isType(ItemStack item) {
        if (item == null)
            return false;
        String name = getModifiedName();
        String otherName = item.getType().name();
        return (otherName.endsWith(name) || (name
                .contentEquals("WEAPON") && (otherName.endsWith("_AXE") || otherName.endsWith("_SWORD"))));
    }

    public String getModifiedName() {
        return (this == BELT) ? LEGGINGS.name() : ((this == AMULET) ? CHESTPLATE.name() : name());
    }

    public static boolean isSimilarItemSkin(ItemSkinType type, ItemSkinType other) {
        return ((other == WEAPON && (type == SWORD || type == AXE)) || type == other);
    }
}

