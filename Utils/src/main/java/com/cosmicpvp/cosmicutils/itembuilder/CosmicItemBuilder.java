/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.math.Range;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class CosmicItemBuilder {
    protected ItemStack item;
    private int minAmount;
    private int maxAmount;

    public CosmicItemBuilder(ItemStack existing) {
        this.item = existing.clone();
    }

    public CosmicItemBuilder(Material type) {
        this.item = new ItemStack(type, 1);
    }

    public CosmicItemBuilder(Material type, String name, String... lore) {
        this.item = new ItemStack(type, 1);
        this.nameAndLore(name, lore);
    }

    public CosmicItemBuilder nameAndLore(String name, List<String> str) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(str);
        this.item.setItemMeta(im);
        return this;
    }

    public CosmicItemBuilder setSkullOwner(String owner) {
        if (this.item == null || !this.item.getType().equals((Object) Material.SKULL_ITEM)) {
            return this;
        }
        SkullMeta sm = (SkullMeta) this.item.getItemMeta();
        sm.setOwner(owner);
        this.item.setItemMeta(sm);
        return this;
    }

    public CosmicItemBuilder nameAndLore(String name, String... lore) {
        return this.nameAndLore(name, Lists.newArrayList(lore));
    }

    public CosmicItemBuilder name(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        this.item.setItemMeta(im);
        return this;
    }

    public CosmicItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public CosmicItemBuilder addLoreLines(List<String> line) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = im.getLore() == null ? Lists.newArrayList() : im.getLore();
        lore.addAll(line);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public CosmicItemBuilder addLoreLine(String... line) {
        return this.addLoreLines(Lists.newArrayList(line));
    }

    public CosmicItemBuilder lore(List<String> lore) {
        ItemMeta im = this.item.getItemMeta();
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public CosmicItemBuilder lore(String... lore) {
        return this.lore(Lists.newArrayList(lore));
    }

    public CosmicItemBuilder data(short data) {
        this.item.setDurability(data);
        return this;
    }

    public CosmicItemBuilder amount(int min, int max) {
        this.minAmount = min;
        this.maxAmount = max;
        return this;
    }

    public CosmicItemBuilder enchant(Enchantment enchant, int level) {
        this.item.addUnsafeEnchantment(enchant, level);
        return this;
    }

    public CosmicItemBuilder removeEnchant(Enchantment enchant) {
        this.item.removeEnchantment(enchant);
        return this;
    }

    public InventoryItem toInventoryItem() {
        return this.toInventoryItem(null);
    }

    public InventoryItem toInventoryItem(Runnable runnable) {
        return new InventoryItem(this).click(runnable);
    }

    public int getAmount() {
        if (this.maxAmount != 0) {
            return Math.max(Range.between(this.minAmount, this.maxAmount), 1);
        }
        return this.item.getAmount();
    }

    public ItemStack build() {
        ItemStack retr = this.item == null ? null : this.item.clone();
        int amount = this.getAmount();
        if (retr != null) {
            retr.setAmount(amount);
        }
        return retr;
    }

    public int getMinAmount() {
        return this.minAmount;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }
}

