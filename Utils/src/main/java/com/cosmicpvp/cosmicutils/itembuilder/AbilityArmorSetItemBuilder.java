package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.struct.EquipmentType;
import me.cosmicpvp.CosmicGlobalKits.libs.MasteryKits;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AbilityArmorSetItemBuilder
        extends ItemBuilder {
    public String armorsetName;
    private EquipmentType type;
    private boolean transmog = false;
    private boolean random;
    private boolean rollMasteryEnchants;

    public AbilityArmorSetItemBuilder(boolean random, boolean rollMasteryEnchants) {
        super(Material.DIRT);
        this.random = random;
        this.rollMasteryEnchants = rollMasteryEnchants;
    }

    public AbilityArmorSetItemBuilder(String armorset, EquipmentType type, boolean transmog) {
        super(Material.DIRT);
        this.type = type;
        this.armorsetName = armorset.toUpperCase();
        this.transmog = transmog;
    }

    @Override
    public ItemStack build() {
        MasteryKits kits;
        if (!CosmicUtils.get().isArmorsetEnabled()) {
            return null;
        }
        EquipmentType type = this.type;
        if (type == null) {
            type = EquipmentType.getRandomArmor();
        }
        MasteryKits masteryKits = kits = this.random ? MasteryKits.values()[ThreadLocalRandom.current().nextInt(MasteryKits.values().length)] : MasteryKits.getFromName(this.armorsetName);
        if (kits != null) {
            List gear = MasteryKits.DEATH_KNIGHT.getKit().getMasteryItems(null, kits, false);
            if (type == EquipmentType.HELMET) {
                return (ItemStack) gear.get(0);
            }
            if (type == EquipmentType.CHESTPLATE) {
                return (ItemStack) gear.get(1);
            }
            if (type == EquipmentType.LEGGINGS) {
                return (ItemStack) gear.get(2);
            }
            if (type == EquipmentType.BOOTS) {
                return (ItemStack) gear.get(3);
            }
        }
        return null;
    }
}

