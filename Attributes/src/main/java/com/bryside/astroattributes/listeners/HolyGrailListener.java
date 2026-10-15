package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.DragonTitanAttributesAPI;
import com.bryside.astroattributes.items.HolyGrail;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HolyGrailListener implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void runeExtract(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            ItemStack cursor = e.getCursor();
            ItemStack current = e.getCurrentItem();
            if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore() && Utils.isWeapon(current)) {
                if (HolyGrail.isHolyGrail(cursor)) {
                    if (DragonTitanAttributesAPI.isRuneCorrupted(current)) {
                        Player player = (Player) e.getWhoClicked();
                        DragonTitanAttributesAPI.removeRuneCorrupt(current);
                        player.sendMessage(CC.Yellow + "** Your weapon is no longer Attribute Corrupted **");
                        if (cursor.getAmount() == 1) {
                            player.setItemOnCursor((ItemStack) null);
                        } else {
                            cursor.setAmount(cursor.getAmount() - 1);
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
