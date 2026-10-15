package net.astropvp.showcasegui.custominventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CustomInventoryItem extends ItemStack {
    private CustomInventoryItemCallback callback;

    public CustomInventoryItem(ItemStack item) {
        super(item);
        this.callback = null;
    }

    public CustomInventoryItem(Material type, int amount, short damage) {
        this(type, amount, damage, (CustomInventoryItemCallback)null);
    }

    public CustomInventoryItem(Material type, int amount, short damage, CustomInventoryItemCallback callback) {
        super(type, amount, damage);
        this.callback = null;
        this.callback = callback;
    }

    public CustomInventoryItemCallback getCallback() {
        return this.callback;
    }

    public void setCallback(CustomInventoryItemCallback callback) {
        this.callback = callback;
    }

    public boolean setItemMeta(String name) {
        return SetItemMeta(this, (String)name);
    }

    public boolean setItemMeta(List<String> lore) {
        return SetItemMeta(this, (List)lore);
    }

    public boolean setItemMeta(String name, String... lore) {
        return SetItemMeta(this, name, lore);
    }

    public static boolean SetItemMeta(ItemStack item, String name) {
        return SetItemMeta(item, name);
    }

    public static boolean SetItemMeta(ItemStack item, List<String> lore) {
        return SetItemMeta(item, (String)null, (String[])lore.toArray(new String[lore.size()]));
    }

    public static boolean SetItemMeta(ItemStack item, String name, String... lore) {
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
        retr.setItemMeta((ItemMeta)this.getItemMeta().clone());
        return retr;
    }

    public static CustomInventoryItemCallback getNoopCallback() {
        return (inventory, customItem, event) -> {
            event.setCancelled(true);
        };
    }
}
