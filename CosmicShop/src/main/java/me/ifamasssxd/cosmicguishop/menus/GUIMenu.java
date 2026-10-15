/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicguishop.menus;

import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class GUIMenu {
    private static Map<UUID, GUIMenu> menus = new HashMap<UUID, GUIMenu>();
    private Inventory menu;
    private Map<Integer, ClickableItemStack> menuItems = new HashMap<Integer, ClickableItemStack>();
    private Consumer<InventoryCloseEvent> closeCallback;
    private String name;
    private int size;

    public GUIMenu(String name, int size) {
        this.name = name;
        this.size = size;
        this.menu = Bukkit.createInventory(null, (int) size, (String) name);
    }

    public static Map<UUID, GUIMenu> getMenus() {
        return menus;
    }

    public void setItem(int slot, ClickableItemStack item) {
        this.menu.setItem(slot, (ItemStack) item);
        this.menuItems.put(slot, item);
    }

    public void open(Player player) {
        GUIMenu openMenu = menus.get(player.getUniqueId());
        if (openMenu != null) {
            player.closeInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicGUIShop.get(), () -> {
                player.openInventory(this.menu);
                menus.put(player.getUniqueId(), this);
            }, 1L);
        } else {
            player.openInventory(this.menu);
            menus.put(player.getUniqueId(), this);
        }
    }

    public Map<Integer, ClickableItemStack> getMenuItems() {
        return this.menuItems;
    }

    public Consumer<InventoryCloseEvent> getCloseCallback() {
        return this.closeCallback;
    }

    public void setCloseCallback(Consumer<InventoryCloseEvent> closeCallback) {
        this.closeCallback = closeCallback;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }
}

