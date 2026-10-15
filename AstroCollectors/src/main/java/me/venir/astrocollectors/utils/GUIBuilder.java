package me.venir.astrocollectors.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class GUIBuilder  implements InventoryHolder {
    protected Inventory inventory;

    protected Player player;

    public GUIBuilder(Player player) {
        this.player = player;
    }

    protected abstract String getName();

    protected abstract int getSlots();

    protected abstract void setItems();

    protected abstract ItemStack getItems(int paramInt);

    public abstract void click(InventoryClickEvent paramInventoryClickEvent);

    public void open() {
        this.inventory = Bukkit.createInventory(this, getSlots(), getName());
        setItems();
        this.player.openInventory(this.inventory);
    }
}