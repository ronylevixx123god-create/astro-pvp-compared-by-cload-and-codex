package me.venir.astrocollectors.listeners.spawnerlisteners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SpawnerBlockPlaceListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void blockPlace(BlockPlaceEvent e) {

        if (e.getBlock().getType() == Material.MOB_SPAWNER) {
            if (AstroSpawners.coolDownSpawnerPlace.contains(e.getPlayer())) {
                e.getPlayer().sendMessage(CC.translate("&c&l(!)&c Please be patient, you are still on spawner place cooldown."));
                e.setCancelled(true);
                return;
            }
            if (ItemSpawner.getSpawnerAmount(e.getItemInHand()) == -1) return;

            int amount = ItemSpawner.getSpawnerAmount(e.getItemInHand());
            EntityType type = ItemSpawner.getEntityTypeFromItem(e.getItemInHand());
            if (type == EntityType.BLAZE) return;
            if (CollectorManager.DELEGATE.isCollectorContained(e.getBlock().getLocation())) {
                Collector collector = CollectorManager.DELEGATE.getCollector(e.getBlock().getLocation());
                if (collector.getSpawnersInChunk().containsKey(type)) {
                    int oldAmount = collector.getSpawnersInChunk().get(type);
                    int newAmount = oldAmount+amount;
                    collector.getSpawnersInChunk().put(type, newAmount);
                } else {
                    collector.getSpawnersInChunk().put(type, amount);
                }
            }
        }
    }
}
