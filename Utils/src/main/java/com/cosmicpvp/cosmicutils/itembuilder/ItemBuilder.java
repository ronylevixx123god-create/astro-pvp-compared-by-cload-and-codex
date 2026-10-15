/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;

import java.text.SimpleDateFormat;
import java.util.*;

public class ItemBuilder {
    private static SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yy");
    protected final List<String> lore = new ArrayList<String>();
    private final HashMap<Enchantment, Integer> enchants = new HashMap();
    private final HashMap<Enchantment, Integer> storedBookEnchants = new HashMap();
    public ItemStack original;
    protected int amount;
    protected String title = null;
    private Material mat;
    private short data;
    private Color color;
    private Potion potion;

    public ItemBuilder(ItemStack original) {
        this.original = original;
    }

    public ItemBuilder(ItemStack original, int amount) {
        this.original = original;
        this.amount = amount;
    }

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

    public ItemBuilder setName(String name) {
        this.title = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public ItemBuilder addStoredEnchantment(Enchantment enchantment, int level) {
        this.storedBookEnchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder addLoreLine(String lore) {
        String line = ChatColor.translateAlternateColorCodes('&', lore);
        if (line.contains("{DATE}")) {
            line = line.replace("{DATE}", format.format(new Date()));
        }
        if (line.contains("{PLAYER}")) {
            line = line.replace("{PLAYER}", CC.GoldB + "???");
        }
        this.lore.add(line);
        return this;
    }

    public ItemBuilder addLoreLines(String... lore) {
        for (String line : lore) {
            this.addLoreLine(line.replace("_", " "));
        }
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add((Object) ((Object) ChatColor.GRAY) + lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        if (this.enchants.containsKey(enchant)) {
            this.enchants.remove(enchant);
        }
        this.enchants.put(enchant, level);
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
        if (this.original != null) {
            if (this.amount != 0) {
                this.original.setAmount(this.amount);
            }
            return this.original;
        }
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
            ((LeatherArmorMeta) meta).setColor(this.color);
        }
        if (!this.storedBookEnchants.isEmpty() && meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta storage = (EnchantmentStorageMeta) meta;
            for (Enchantment ench2 : storage.getStoredEnchants().keySet()) {
                storage.removeStoredEnchant(ench2);
            }
            this.storedBookEnchants.forEach((ench, level) -> storage.addStoredEnchant((Enchantment) ench, (int) level, true));
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
        newBuilder.setTitle(this.title);
        for (String string : this.lore) {
            newBuilder.addLore(string);
        }
        for (Map.Entry entry : this.enchants.entrySet()) {
            newBuilder.addEnchantment((Enchantment) entry.getKey(), (Integer) entry.getValue());
        }
        newBuilder.setColor(this.color);
        newBuilder.potion = this.potion;
        return newBuilder;
    }

    public Material getType() {
        return this.mat;
    }

    public ItemBuilder setType(Material mat) {
        this.mat = mat;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Color getColor() {
        return this.color;
    }

    public ItemBuilder setColor(Color color) {
        if (this.mat.name().contains("LEATHER_")) {
            this.color = color;
        }
        return this;
    }

    public boolean hasEnchantment(Enchantment enchant) {
        return this.enchants.containsKey(enchant);
    }

    public int getEnchantmentLevel(Enchantment enchant) {
        return this.enchants.get(enchant);
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

