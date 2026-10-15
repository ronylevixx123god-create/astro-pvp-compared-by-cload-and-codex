/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(ItemStack existing) {
        this.item = existing.clone();
    }

    public ItemBuilder(Material type) {
        this.item = new ItemStack(type, 1);
    }

    public ItemBuilder(Material type, String name, String... lore) {
        this.item = new ItemStack(type, 1);
        this.nameAndLore(name, lore);
    }

    public ItemBuilder nameAndLore(String name, List<String> str) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(str);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        if (this.item == null || !this.item.getType().equals((Object) Material.SKULL_ITEM)) {
            return this;
        }
        SkullMeta sm = (SkullMeta) this.item.getItemMeta();
        sm.setOwner(owner);
        this.item.setItemMeta(sm);
        return this;
    }

    public ItemBuilder nameAndLore(String name, String... lore) {
        return this.nameAndLore(name, Lists.newArrayList(lore));
    }

    public ItemBuilder name(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder addLoreLines(List<String> line) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = im.getLore() == null ? Lists.newArrayList() : im.getLore();
        lore.addAll(line);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String... line) {
        return this.addLoreLines(Lists.newArrayList(line));
    }

    public ItemBuilder lore(List<String> lore) {
        ItemMeta im = this.item.getItemMeta();
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        return this.lore(Lists.newArrayList(lore));
    }

    public ItemBuilder data(short data) {
        this.item.setDurability(data);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        this.item.addUnsafeEnchantment(enchant, level);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchant) {
        this.item.removeEnchantment(enchant);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}

