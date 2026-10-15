/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

final class InventoryScannerListener
        implements Listener {
    InventoryScannerListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        String invName = e.getInventory().getName();
        if (invName.equals("container.crafting") && e.getCurrentItem() != null && this.hasAHLore(e.getCurrentItem())) {
            e.setCurrentItem(null);
            Bukkit.getLogger().info("[Arkkit (AntiDupe)] Deleting AHItem from container.crafting of " + e.getWhoClicked().getName());
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (this.hasAHLore(e.getItem().getItemStack())) {
            e.getItem().remove();
        }
    }

    private boolean hasAHLore(ItemStack i) {
        if (i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String s : i.getItemMeta().getLore()) {
                if (!s.startsWith(ChatColor.GOLD.toString() + "Price: " + ChatColor.YELLOW + "$")) continue;
                return true;
            }
        }
        return false;
    }
}

