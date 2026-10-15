package me.astropvp.astrospawners.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.items.ItemSpawner;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CombineSpawnersInventoryListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCursor() != null) {
            if (event.getCurrentItem().getType() != Material.MOB_SPAWNER
                    || event.getCursor().getType() != Material.MOB_SPAWNER) {
                return;
            }
            int amount = ItemSpawner.getSpawnerAmount(event.getCursor());
            int other = ItemSpawner.getSpawnerAmount(event.getCurrentItem());
            EntityType type = ItemSpawner.getEntityTypeFromItem(event.getCursor());
            EntityType otherType = ItemSpawner.getEntityTypeFromItem(event.getCurrentItem());
            if (amount <= 0 || other <= 0 || otherType != type) {
                return;
            }
            amount += other;
            if(event.getCursor().getAmount() > 1) {
                event.getCursor().setAmount(event.getCursor().getAmount() - 1);
            } else {
                event.setCursor(null);
            }
            event.setCurrentItem(ItemSpawner.getItem(type, amount));
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_USE, 0.75F, 2F);
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.LEVEL_UP, 0.75F, 2F);
            event.setCancelled(true);
        }
    }
}
