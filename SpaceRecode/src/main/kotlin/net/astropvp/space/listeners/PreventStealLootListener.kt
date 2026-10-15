package net.astropvp.space.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class PreventStealLootListener : Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.inventory.title != null) {
            if (e.inventory.title.contains(" Loot Items")) {
                e.isCancelled = true
            }
        }
    }

}