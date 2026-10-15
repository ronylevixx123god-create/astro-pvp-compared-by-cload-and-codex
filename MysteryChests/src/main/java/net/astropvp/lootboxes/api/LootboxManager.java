/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.astropvp.lootboxes.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LootboxManager {
    String getNBTCompound();

    String getNBTIsLootbox();

    String getNBTLootboxType();

    void addNewLootbox(Lootbox var1);

    Optional<Lootbox> getLootbox(String var1);

    boolean removeLootbox(String var1);

    List<Lootbox> getLootboxes();

    Optional<Lootbox> getLootboxFrom(ItemStack var1);

    Set<Material> getLootboxMaterials();

    void forceSave(boolean var1);
}

