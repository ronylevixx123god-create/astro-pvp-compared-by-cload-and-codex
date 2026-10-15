package com.cosmicpvp.cosmicwildpvp.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public interface Menu extends InventoryHolder
{
    default void onClick(final Player player, final int slot, final ClickType type, final InventoryClickEvent event) {
    }
}
