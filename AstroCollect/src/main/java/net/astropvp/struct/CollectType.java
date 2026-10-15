package net.astropvp.struct;

import org.bukkit.Material;

public enum CollectType {
    DEATH_INVENTORY(Material.DIAMOND_SWORD),
    SLOTBOT(Material.PAPER),
    CRTAEKEY(Material.TRIPWIRE_HOOK),
    ITEMFLIP(Material.ENCHANTMENT_TABLE),
    DUEL(Material.DIAMOND_CHESTPLATE),
    HYPEBOX_BATTLE(Material.BEACON),
    BAH(Material.ENDER_CHEST),
    GIVEALL(Material.GOLD_NUGGET),
    SPACE(Material.GLASS),

    ;

    Material material;

    CollectType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return this.material;
    }
}
