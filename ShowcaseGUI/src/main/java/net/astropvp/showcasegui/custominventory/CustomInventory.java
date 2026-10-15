package net.astropvp.showcasegui.custominventory;

import net.astropvp.showcasegui.ShowcaseGUI;
import net.astropvp.showcasegui.commands.CommandMergeShowcase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class CustomInventory {
    protected Map<Integer, CustomInventoryItem> inventoryItems = new TreeMap();
    protected final String title;
    protected final int rows;
    protected final InventoryType type;
    protected CustomInventoryCallback callback = null;
    protected CustomInventoryBottomCallback bottomCallback = null;
    protected CustomInventoryTopCallback topCallback;
    public Inventory inventoryChest;
    private static Map<UUID, CustomInventory> playersCustomInventory = new HashMap();

    public CustomInventory(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.type = null;
        Inventory customChest;
        if (this.type == null) {
            customChest = Bukkit.getServer().createInventory((InventoryHolder)null, rows * 9, title);
        } else {
            customChest = Bukkit.getServer().createInventory((InventoryHolder)null, this.type, title);
        }

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
            if (p != null) {
                p.closeInventory();
            }
        }

    }

    public static boolean hasCustomInventory(Player player) {
        return playersCustomInventory.containsKey(player.getUniqueId());
    }

    public static CustomInventory getCustomInventory(Player player) {
        return (CustomInventory)playersCustomInventory.get(player.getUniqueId());
    }

    public static boolean removeInventory(Player player) {
        return playersCustomInventory.remove(player.getUniqueId()) != null;
    }

    public void openInventory(Player player, boolean delayClose) {
        if (CommandMergeShowcase.merging) {
            player.sendMessage((String)(ChatColor.RED + "Please wait while the Showcases are being merged."));
        } else {
            CustomInventory current = (CustomInventory)playersCustomInventory.get(player.getUniqueId());
            if (current == null) {
                this.updateInventoryItems();
                player.openInventory(this.inventoryChest);
                playersCustomInventory.put(player.getUniqueId(), this);
            } else if (current.equals(this)) {
                this.updateInventoryItems();
            } else {
                if (delayClose) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable)(() -> {
                        player.closeInventory();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable)(() -> {
                            this.applyInventory(player);
                        }), 1L);
                    }), 1L);
                } else {
                    player.closeInventory();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable)(() -> {
                        this.applyInventory(player);
                    }), 1L);
                }

            }
        }
    }

    public void applyInventory(Player player) {
        player.openInventory(this.inventoryChest);
        this.updateInventoryItems();
        playersCustomInventory.put(player.getUniqueId(), this);
    }

    protected CustomInventoryItem[] getInventoryItems() {
        CustomInventoryItem[] inventoryItems = new CustomInventoryItem[this.getInventorySize()];

        for(int idx = 0; idx < inventoryItems.length; ++idx) {
            inventoryItems[idx] = (CustomInventoryItem)this.inventoryItems.get(idx);
        }

        return inventoryItems;
    }

    protected void setInventoryItems(CustomInventoryItem[] inventoryItems) {
        this.inventoryChest.setContents((ItemStack[])inventoryItems);
    }

    public void updateInventoryItems() {
        this.setInventoryItems(this.getInventoryItems());
    }

    public void clearInventoryItems() {
        this.inventoryChest.clear();
        this.inventoryItems.clear();
    }

    public CustomInventoryItem insertItem(int slot, CustomInventoryItem item) {
        return (CustomInventoryItem)this.inventoryItems.put(slot, item);
    }

    public CustomInventoryItem insertItem(int slot, CustomInventoryItem item, boolean update) {
        CustomInventoryItem ts = (CustomInventoryItem)this.inventoryItems.put(slot, item);
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
        } else {
            this.insertItem(slot, is, true);
            return true;
        }
    }

    public int getOpenSlot() {
        for(int slot = 0; slot < this.getInventorySize(); ++slot) {
            if (this.getItem(slot) == null) {
                return slot;
            }
        }

        return -1;
    }

    public CustomInventoryItem removeItem(int slot) {
        return (CustomInventoryItem)this.inventoryItems.remove(slot);
    }

    public CustomInventoryItem removeItem(int slot, boolean update) {
        CustomInventoryItem item = (CustomInventoryItem)this.inventoryItems.remove(slot);
        if (update) {
            this.inventoryChest.setItem(slot, (ItemStack)null);
        }

        return item;
    }

    public CustomInventoryItem removeItem(CustomInventoryItem item) {
        Iterator var2 = this.getItems().entrySet().iterator();

        Map.Entry items;
        do {
            if (!var2.hasNext()) {
                return item;
            }

            items = (Map.Entry)var2.next();
        } while(items.getValue() == null || !((CustomInventoryItem)items.getValue()).getItemMeta().equals(item.getItemMeta()) || ((CustomInventoryItem)items.getValue()).getAmount() != item.getAmount() || !((CustomInventoryItem)items.getValue()).equals(item));

        this.removeItem((Integer)items.getKey(), true);
        return item;
    }

    public Map<Integer, CustomInventoryItem> getItems() {
        return Collections.unmodifiableMap(this.inventoryItems);
    }

    public int getEmptySlots() {
        int empty = 0;
        ListIterator var2 = this.inventoryChest.iterator();

        while(true) {
            ItemStack is;
            do {
                if (!var2.hasNext()) {
                    return empty;
                }

                is = (ItemStack)var2.next();
            } while(is != null && is.getType() != Material.AIR);

            ++empty;
        }
    }

    public CustomInventoryItem getItem(int slot) {
        return (CustomInventoryItem)this.inventoryItems.get(slot);
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
}
