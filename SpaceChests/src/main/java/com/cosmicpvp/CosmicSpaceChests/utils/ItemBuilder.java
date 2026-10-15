/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.LeatherArmorMeta
 *  org.bukkit.potion.Potion
 */
package com.cosmicpvp.CosmicSpaceChests.utils;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;

public class ItemBuilder {
    private Material mat;
    private int amount;
    private final short data;
    private String title = null;
    private final List<String> lore = new ArrayList<String>();
    private final HashMap<Enchantment, Integer> enchants = new HashMap();
    private Color color;
    private Potion potion;

    public ItemBuilder(Material mat) {
        this(mat, 1);
    }

    public ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short) 0);
    }

    public ItemBuilder(Material mat, short data) {
        this(mat, 1, data);
    }

    public ItemBuilder(Material mat, int amount, short data) {
        this.mat = mat;
        this.amount = amount;
        this.data = data;
    }

    public ItemBuilder setType(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemBuilder setTitle(String title, ItemRarity ir) {
        ChatColor cc = ChatColor.GRAY;
        if (ir == ItemRarity.SIMPLE) {
            cc = ChatColor.WHITE;
        }
        if (ir == ItemRarity.UNIQUE) {
            cc = ChatColor.GREEN;
        }
        if (ir == ItemRarity.ELITE) {
            cc = ChatColor.AQUA;
        }
        if (ir == ItemRarity.ULTIMATE) {
            cc = ChatColor.YELLOW;
        }
        this.title = (Object)cc + title;
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add((Object)ChatColor.GRAY + lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        if (this.enchants.containsKey((Object)enchant)) {
            this.enchants.remove((Object)enchant);
        }
        this.enchants.put(enchant, level);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (this.mat.name().contains("LEATHER_")) {
            this.color = color;
        }
        return this;
    }

    public ItemBuilder setPotion(Potion potion) {
        if (this.mat != Material.POTION) {
            this.mat = Material.POTION;
        }
        this.potion = potion;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        Material mat = this.mat;
        if (mat == null) {
            mat = Material.AIR;
            return new ItemStack(Material.AIR);
        }
        ItemStack item = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();
        if (this.title != null) {
            meta.setDisplayName(this.title);
        }
        if (!this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)meta).setColor(this.color);
        }
        item.setItemMeta(meta);
        item.addUnsafeEnchantments(this.enchants);
        if (this.potion != null) {
            this.potion.apply(item);
        }
        return item;
    }

    public ItemBuilder clone() {
        ItemBuilder newBuilder = new ItemBuilder(this.mat);
        newBuilder.setTitle(this.title, null);
        for (String string : this.lore) {
            newBuilder.addLore(string);
        }
        for (Map.Entry entry : this.enchants.entrySet()) {
            newBuilder.addEnchantment((Enchantment)entry.getKey(), (Integer)entry.getValue());
        }
        newBuilder.setColor(this.color);
        newBuilder.potion = this.potion;
        return newBuilder;
    }

    public Material getType() {
        return this.mat;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean hasEnchantment(Enchantment enchant) {
        return this.enchants.containsKey((Object)enchant);
    }

    public int getEnchantmentLevel(Enchantment enchant) {
        return this.enchants.get((Object)enchant);
    }

    public HashMap<Enchantment, Integer> getAllEnchantments() {
        return this.enchants;
    }

    public boolean isItem(ItemStack item) {
        if (item == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != this.getType()) {
            return false;
        }
        if (!meta.hasDisplayName() && this.getTitle() != null) {
            return false;
        }
        if (!meta.getDisplayName().equals(this.getTitle())) {
            return false;
        }
        if (!meta.hasLore() && !this.getLore().isEmpty()) {
            return false;
        }
        if (meta.hasLore()) {
            for (String lore : meta.getLore()) {
                if (this.getLore().contains(lore)) continue;
                return false;
            }
        }
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            if (this.hasEnchantment(enchant)) continue;
            return false;
        }
        return true;
    }
}

