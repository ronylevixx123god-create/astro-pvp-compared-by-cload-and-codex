package com.cosmicpvp.cosmicwildpvp.listeners;

import com.cosmicpvp.cosmicwildpvp.util.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public class MenuListener implements Listener
{
    @EventHandler(ignoreCancelled = true)
    private void onClick(final InventoryClickEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Menu)) {
            return;
        }
        ((Menu)holder).onClick((Player)event.getWhoClicked(), event.getSlot(), event.getClick(), event);
    }
}
