package net.astropvp.core.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CheckCreativeDupeListener implements Listener {

    @EventHandler
    public void checkClick(InventoryClickEvent event) {
        if(event.getClick() == ClickType.CREATIVE) {
            if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            ItemStack itemStack = event.getCurrentItem();
            event.setCurrentItem(addDataToItem(itemStack, (Player) event.getWhoClicked()));
        }
    }


    private ItemStack addDataToItem(ItemStack itemStack, Player player) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("dupedBy", player.getName());
        nbtItem.setLong("dupedAt", System.currentTimeMillis());
        return nbtItem.getItem();
    }
}
