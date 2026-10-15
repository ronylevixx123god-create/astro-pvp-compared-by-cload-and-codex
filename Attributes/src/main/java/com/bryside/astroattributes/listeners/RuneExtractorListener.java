package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.DragonTitanAttributesAPI;
import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.events.RuneExtractEvent;
import com.bryside.astroattributes.items.RuneExtractor;
import com.bryside.astroattributes.objects.RuneCacher;
import com.bryside.astroattributes.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RuneExtractorListener implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void runeExtract(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            ItemStack cursor = e.getCursor();
            ItemStack current = e.getCurrentItem();
            if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore() && Utils.isWeapon(current)) {
                if (RuneExtractor.isRuneExtractor(cursor)) {
                    Player player = (Player) e.getWhoClicked();
                    RuneCacher cacher = new RuneCacher(current);
                    RuneType type = cacher.getRuneType();
                    int experience = cacher.getExperience();
                    int level = cacher.getLevel();
                    if (type != null && experience != -1 && level != -1) {
                        if (cursor.getAmount() == 1) {
                            player.setItemOnCursor((ItemStack) null);
                        } else {
                            cursor.setAmount(cursor.getAmount() - 1);
                        }

                        e.setCancelled(true);
                        RuneExtractEvent event = new RuneExtractEvent(player, current, type, RuneExtractor.getRuneExtractorSuccessRate(cursor), level, experience);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void runeExtract(RuneExtractEvent e) {
        ItemStack item = e.getItem();
        e.getPlayer().getInventory().addItem(new ItemStack[]{DragonTitanAttributesAPI.extractRune(item, e.getSuccess())});
        DragonTitanAttributesAPI.runeCorruptWeapon(item);
    }
}
