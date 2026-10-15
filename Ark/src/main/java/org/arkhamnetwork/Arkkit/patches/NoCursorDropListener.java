/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

final class NoCursorDropListener
        implements Listener {
    NoCursorDropListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent e) {
        Player pl = (Player) e.getPlayer();
        if (pl.getItemOnCursor() != null && pl.getInventory().firstEmpty() != -1 && !pl.isDead() && pl.getHealth() > 0.0) {
            ItemStack i = pl.getItemOnCursor().clone();
            pl.setItemOnCursor(null);
            pl.getInventory().addItem(i);
            pl.updateInventory();
        }
    }
}

