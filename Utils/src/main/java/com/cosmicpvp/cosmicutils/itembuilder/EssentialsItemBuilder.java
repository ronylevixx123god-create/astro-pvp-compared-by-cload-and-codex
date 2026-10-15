/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EssentialsItemBuilder
        extends ItemBuilder {
    private String essentialsString;
    private ItemStack parsedItem;

    public EssentialsItemBuilder(String essentialsString) {
        super((Material) null, 0);
        this.essentialsString = essentialsString;
        List<ItemStack> retr = ItemUtils.parseItemsIntoItemStacks(Lists.newArrayList(essentialsString));
        if (retr == null) {
            Bukkit.getLogger().info("Unable to parse item from " + essentialsString + "!");
        } else if (!retr.isEmpty()) {
            this.parsedItem = retr.get(0);
        }
    }

    @Override
    public ItemBuilder setName(String name) {
        return super.setName(name.replace("_", " "));
    }

    @Override
    public ItemBuilder addLore(String lore) {
        return super.addLoreLine(lore.replace("_", " "));
    }

    @Override
    public ItemStack build() {
        if (this.parsedItem == null) {
            Bukkit.getLogger().info("Null essentials item: " + this.essentialsString);
            return new ItemStack(Material.AIR);
        }
        ItemMeta meta = this.parsedItem.getItemMeta();
        if (this.title != null) {
            meta.setDisplayName(this.title);
        }
        if (!this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }
        this.parsedItem.setItemMeta(meta);
        return this.parsedItem;
    }
}

