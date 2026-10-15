package net.astropvp.showcasegui.custominventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomInventoryBottomCallback {
    void inventoryClickEvent(CustomInventory var1, ItemStack var2, InventoryClickEvent var3);
}
