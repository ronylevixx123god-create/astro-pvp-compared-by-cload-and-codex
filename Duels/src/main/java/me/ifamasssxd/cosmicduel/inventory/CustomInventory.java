/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.InventoryHolder
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class CustomInventory {
    protected Map<Integer, CustomInventoryItem> inventoryItems = new TreeMap<Integer, CustomInventoryItem>();
    protected final String title;
    protected final int rows;
    protected final InventoryType type;
    protected CustomInventoryCallback callback = null;
    protected CustomInventoryBottomCallback bottomCallback = null;
    protected CustomInventoryTopCallback topCallback;
    public Inventory inventoryChest;
    private static final Map<UUID, CustomInventory> playersCustomInventory = new HashMap<UUID, CustomInventory>();

    public CustomInventory(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.type = null;
        Inventory customChest = this.type == null ? Bukkit.getServer().createInventory(null, rows * 9, title) : Bukkit.getServer().createInventory(null, this.type, title);
        this.inventoryChest = customChest;
    }

    public CustomInventory(String title, InventoryType type) {
        this.title = title;
        this.rows = 0;
        this.type = type;
    }

    public static void closeInventories() {
        for (UUID uuid : playersCustomInventory.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;
            p.closeInventory();
        }
    }

    public static boolean hasCustomInventory(Player player) {
        return playersCustomInventory.containsKey(player.getUniqueId());
    }

    public static CustomInventory getCustomInventory(Player player) {
        return playersCustomInventory.get(player.getUniqueId());
    }

    public static boolean removeInventory(Player player) {
        return playersCustomInventory.remove(player.getUniqueId()) != null;
    }

    public void openInventory(Player player) {
        if (CustomInventory.getCustomInventory(player) != null) {
            this.updateInventoryItems();
        }
        player.openInventory(this.inventoryChest);
        playersCustomInventory.put(player.getUniqueId(), this);
    }

    protected CustomInventoryItem[] getInventoryItems() {
        CustomInventoryItem[] inventoryItems = new CustomInventoryItem[this.getInventorySize()];
        for (int idx = 0; idx < inventoryItems.length; ++idx) {
            inventoryItems[idx] = this.inventoryItems.get(idx);
        }
        return inventoryItems;
    }

    protected void setInventoryItems(CustomInventoryItem[] inventoryItems) {
        this.inventoryChest.setContents(inventoryItems);
    }

    public void updateInventoryItems() {
        this.setInventoryItems(this.getInventoryItems());
    }

    public void clearInventoryItems() {
        this.inventoryChest.clear();
        this.inventoryItems.clear();
    }

    public CustomInventoryItem insertItem(int slot, CustomInventoryItem item) {
        return this.inventoryItems.put(slot, item);
    }

    public CustomInventoryItem insertItem(int slot, CustomInventoryItem item, boolean update) {
        CustomInventoryItem ts = this.inventoryItems.put(slot, item);
        if (update) {
            this.inventoryChest.setItem(slot, item);
        }
        return ts;
    }

    public CustomInventoryItem insertItem(int row, int col, CustomInventoryItem item) {
        return this.insertItem(row * 9 + col, item);
    }

    public boolean addItem(CustomInventoryItem is) {
        int slot = this.getOpenSlot();
        if (slot == -1) {
            return false;
        }
        this.insertItem(slot, is, true);
        return true;
    }

    public int getOpenSlot() {
        for (int slot = 0; slot < this.getInventorySize(); ++slot) {
            if (this.getItem(slot) != null) continue;
            return slot;
        }
        return -1;
    }

    public CustomInventoryItem removeItem(int slot) {
        return this.inventoryItems.remove(slot);
    }

    public CustomInventoryItem removeItem(int slot, boolean update) {
        CustomInventoryItem item = this.inventoryItems.remove(slot);
        if (update) {
            this.inventoryChest.setItem(slot, null);
        }
        return item;
    }

    public CustomInventoryItem removeItem(CustomInventoryItem item) {
        for (Map.Entry<Integer, CustomInventoryItem> items : this.getItems().entrySet()) {
            if (items.getValue() == null || !items.getValue().getItemMeta().equals(item.getItemMeta()) || items.getValue().getAmount() != item.getAmount() || !items.getValue().equals(item)) continue;
            this.removeItem(items.getKey(), true);
            return item;
        }
        return item;
    }

    public Map<Integer, CustomInventoryItem> getItems() {
        return Collections.unmodifiableMap(this.inventoryItems);
    }

    public int getEmptySlots() {
        int empty = 0;
        for (ItemStack is : this.inventoryChest) {
            if (is != null && is.getType() != Material.AIR) continue;
            ++empty;
        }
        return empty;
    }

    public CustomInventoryItem getItem(int slot) {
        return this.inventoryItems.get(slot);
    }

    public int getInventorySize() {
        return this.inventoryChest == null ? this.rows * 9 : this.inventoryChest.getSize();
    }

    public CustomInventoryCallback getCallback() {
        return this.callback;
    }

    public void setCallback(CustomInventoryCallback callback) {
        this.callback = callback;
    }

    public String getName() {
        return this.title;
    }

    public int getRows() {
        return this.rows;
    }

    public CustomInventoryBottomCallback getBottomCallback() {
        return this.bottomCallback;
    }

    public void setBottomCallback(CustomInventoryBottomCallback bottomCallback) {
        this.bottomCallback = bottomCallback;
    }

    public CustomInventoryTopCallback getTopCallback() {
        return this.topCallback;
    }

    public void setTopCallback(CustomInventoryTopCallback topCallback) {
        this.topCallback = topCallback;
    }

    public static Map<UUID, CustomInventory> getPlayersCustomInventory() {
        return playersCustomInventory;
    }
}

