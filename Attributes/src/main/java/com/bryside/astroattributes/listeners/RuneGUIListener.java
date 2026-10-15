package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.enums.RuneType;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RuneGUIListener implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getCurrentItem() != null) {
                if (e.getInventory().getTitle().contentEquals(CC.DarkGray + "Astro Attributes")) {
                    Player player = (Player) e.getWhoClicked();
                    if (player.isOp()) {
                        ItemStack item = e.getCurrentItem();
                        if (RuneType.getType(item) != null) {
                            player.getInventory().addItem(new ItemStack[]{e.getCurrentItem()});
                        }
                    }

                    e.setCancelled(true);
                }

            }
        }
    }
}
