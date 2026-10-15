/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicduel.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CustomInventoryItem
extends ItemStack {
    private CustomInventoryItemCallback callback = null;

    public CustomInventoryItem(ItemStack item) {
        super(item);
    }

    public CustomInventoryItem(ItemStack item, CustomInventoryItemCallback callback) {
        super(item);
        this.callback = callback;
    }

    public CustomInventoryItem(Material type, int amount, short damage) {
        this(type, amount, damage, null);
    }

    public CustomInventoryItem(Material type, int amount, short damage, CustomInventoryItemCallback callback) {
        super(type, amount, damage);
        this.callback = callback;
    }

    public CustomInventoryItemCallback getCallback() {
        return this.callback;
    }

    public void setCallback(CustomInventoryItemCallback callback) {
        this.callback = callback;
    }

    public boolean setItemMeta(String name) {
        return CustomInventoryItem.SetItemMeta((ItemStack)this, name);
    }

    public boolean setItemMeta(List<String> lore) {
        return CustomInventoryItem.SetItemMeta((ItemStack)this, lore);
    }

    public boolean setItemMeta(String name, String ... lore) {
        return CustomInventoryItem.SetItemMeta(this, name, lore);
    }

    public static boolean SetItemMeta(ItemStack item, String name) {
        return CustomInventoryItem.SetItemMeta(item, name, new String[0]);
    }

    public static boolean SetItemMeta(ItemStack item, List<String> lore) {
        return CustomInventoryItem.SetItemMeta(item, null, lore.toArray(new String[lore.size()]));
    }

    public static boolean SetItemMeta(ItemStack item, String name, String ... lore) {
        ItemMeta newMeta = item.getItemMeta();
        if (name != null) {
            newMeta.setDisplayName(name);
        }
        if (lore != null && lore.length > 0) {
            newMeta.setLore(Arrays.asList(lore));
        }
        return item.setItemMeta(newMeta);
    }

    public CustomInventoryItem copy() {
        CustomInventoryItem retr = this.copyNoCallback();
        retr.setCallback(this.callback);
        return retr;
    }

    public CustomInventoryItem copyNoCallback() {
        CustomInventoryItem retr = new CustomInventoryItem(this.getType(), this.getAmount(), this.getDurability(), this.getCallback());
        retr.setItemMeta(this.getItemMeta().clone());
        return retr;
    }

    public static CustomInventoryItemCallback getNoopCallback() {
        return (inventory, customItem, event) -> event.setCancelled(true);
    }
}

