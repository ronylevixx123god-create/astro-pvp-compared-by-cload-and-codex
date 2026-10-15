/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicDungeons.DungeonsAPI
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.compilex.CosmicDungeons.DungeonsAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DungeonPortalIngredientBuilder
        extends ItemBuilder {
    private PortalIngredient ingredient;

    public DungeonPortalIngredientBuilder(PortalIngredient ingredient) {
        super(Material.BONE);
        this.ingredient = ingredient;
    }

    @Override
    public ItemStack build() {
        ItemStack retr = null;
        if (this.ingredient == PortalIngredient.ENCHANTED_OBSIDIAN) {
            retr = DungeonsAPI.get().getEnchantedObsidian();
        } else if (this.ingredient == PortalIngredient.DIMENSIONAL_WEB) {
            retr = DungeonsAPI.get().getDimensionWeb();
        } else if (this.ingredient == PortalIngredient.COSMNOAUT_FUEL) {
            retr = DungeonsAPI.get().getCosmonautFuel();
        } else if (this.ingredient == PortalIngredient.INTERDIMENSIONAL_CORE) {
            retr = DungeonsAPI.get().getInterdimensionalCore();
        } else if (this.ingredient == PortalIngredient.TELEPORTATION_CRYSTAL) {
            retr = DungeonsAPI.get().getTeleportationCrystal();
        }
        if (this.amount > 1 && retr != null) {
            retr.setAmount(this.amount);
        }
        return retr;
    }

    public static enum PortalIngredient {
        ENCHANTED_OBSIDIAN,
        DIMENSIONAL_WEB,
        COSMNOAUT_FUEL,
        INTERDIMENSIONAL_CORE,
        TELEPORTATION_CRYSTAL;

    }
}

