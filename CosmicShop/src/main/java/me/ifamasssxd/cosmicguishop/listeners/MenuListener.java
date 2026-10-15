/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicguishop.listeners;

import me.ifamasssxd.cosmicguishop.menus.ClickableItemStack;
import me.ifamasssxd.cosmicguishop.menus.GUIMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener
        implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals("Shop Categories") || event.getInventory().getName().startsWith("Shop Items") || event.getInventory().getName().startsWith("Confirm Inventory Purchase")) {
            event.setCancelled(true);
        }
        Player player = (Player) event.getWhoClicked();
        GUIMenu menu = GUIMenu.getMenus().get(player.getUniqueId());
        if (menu != null) {
            event.setCancelled(true);
            if (!menu.getName().equals(event.getInventory().getName())) {
                player.closeInventory();
                return;
            }
            ItemStack item = event.getCurrentItem();
            if (item == null) {
                return;
            }
            if (event.getRawSlot() >= event.getInventory().getSize()) {
                return;
            }
            ClickableItemStack found = menu.getMenuItems().get(event.getRawSlot());
            if (found != null && found.isSimilar(item) && found.getItemCallback() != null) {
                found.getItemCallback().onClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GUIMenu menu = GUIMenu.getMenus().remove(event.getPlayer().getUniqueId());
        if (menu != null && menu.getCloseCallback() != null) {
            menu.getCloseCallback().accept(event);
        }
    }

    @EventHandler
    public void onPLayerLeave(PlayerQuitEvent event) {
        GUIMenu menu = GUIMenu.getMenus().remove(event.getPlayer().getUniqueId());
        if (menu != null && menu.getCloseCallback() != null) {
            menu.getCloseCallback().accept(new InventoryCloseEvent(event.getPlayer().getOpenInventory()));
        }
    }
}

