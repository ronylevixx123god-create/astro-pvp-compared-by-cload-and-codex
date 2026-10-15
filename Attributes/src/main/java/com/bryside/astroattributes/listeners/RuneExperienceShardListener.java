package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.DragonTitanAttributesAPI;
import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.events.RuneExperienceIncreaseEvent;
import com.bryside.astroattributes.items.RuneExperienceShard;
import com.bryside.astroattributes.objects.RuneCacher;
import com.bryside.astroattributes.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RuneExperienceShardListener implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void runeExtract(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            ItemStack cursor = e.getCursor();
            ItemStack current = e.getCurrentItem();
            if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore() && Utils.isWeapon(current)) {
                if (RuneExperienceShard.isRuneExperienceShard(cursor)) {
                    Player player = (Player) e.getWhoClicked();
                    RuneCacher cacher = new RuneCacher(current);
                    RuneType type = cacher.getRuneType();
                    int experience = cacher.getExperience();
                    int level = cacher.getLevel();
                    if (type != null && experience != -1 && level != -1 && level < cacher.getRuneType().getRune().getMaxLevel()) {
                        if (cursor.getAmount() == 1) {
                            player.setItemOnCursor((ItemStack) null);
                        } else {
                            cursor.setAmount(cursor.getAmount() - 1);
                        }

                        e.setCancelled(true);
                        RuneExperienceIncreaseEvent event = new RuneExperienceIncreaseEvent(player, current, RuneExperienceShard.getExperienceOnShard(cursor));
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
    public void runeExtract(RuneExperienceIncreaseEvent e) {
        ItemStack item = e.getItem();
        int exp = e.getExperience();
        DragonTitanAttributesAPI.addExperienceToRune(item, exp);
    }
}
