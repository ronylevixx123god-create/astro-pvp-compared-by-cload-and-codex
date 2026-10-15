package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.struct.EquipmentType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArmorSetItemBuilder
        extends ItemBuilder {
    public String armorsetName;
    private EquipmentType type;
    private boolean transmog = false;
    private double weaponChance = 0.2;

    public ArmorSetItemBuilder(String armorset, EquipmentType type, boolean transmog) {
        super(Material.DIRT);
        this.type = type;
        this.armorsetName = armorset;
        this.transmog = transmog;
    }

    public ArmorSetItemBuilder setWeaponChance(double val) {
        this.weaponChance = val;
        return this;
    }

    @Override
    public ItemStack build() {
return null;
    }
}

