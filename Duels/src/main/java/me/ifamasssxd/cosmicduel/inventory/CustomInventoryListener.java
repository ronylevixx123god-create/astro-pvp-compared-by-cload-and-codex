/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.ClickType
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 */
package me.ifamasssxd.cosmicduel.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CustomInventoryListener
implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player)event.getPlayer();
            CustomInventory inventory = CustomInventory.getCustomInventory(player);
            if (inventory == null) {
                return;
            }
            if (inventory.getCallback() != null) {
                inventory.getCallback().inventoryCloseEvent(inventory, event);
            }
            CustomInventory.removeInventory(player);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            CustomInventory inventory = CustomInventory.getCustomInventory(player);
            if (inventory == null) {
                return;
            }
            if (event.getClick() == ClickType.NUMBER_KEY || event.isShiftClick()) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
            int slot = event.getRawSlot();
            if (slot >= inventory.getInventorySize()) {
                if (inventory.getBottomCallback() != null) {
                    inventory.getBottomCallback().inventoryClickEvent(inventory, event.getCurrentItem(), event);
                } else {
                    event.setCancelled(true);
                }
                return;
            }
            CustomInventoryItem customItem = inventory.getItem(slot);
            if (inventory.getTopCallback() != null) {
                inventory.getTopCallback().inventoryClickEvent(inventory, customItem, event);
            } else {
                if (customItem == null) {
                    return;
                }
                if (customItem.getCallback() != null) {
                    customItem.getCallback().inventoryClickEvent(inventory, customItem, event);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (CustomInventory.hasCustomInventory(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

