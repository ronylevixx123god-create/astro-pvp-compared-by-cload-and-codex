package net.astropvp.showcasegui.custominventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface CustomInventoryItemCallback {
    void inventoryClickEvent(CustomInventory var1, CustomInventoryItem var2, InventoryClickEvent var3);
}
