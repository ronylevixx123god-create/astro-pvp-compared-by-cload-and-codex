package me.venir.astrocollectors.listeners.spawnerlisteners;

import me.astropvp.astrospawners.AstroSpawners;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class SpawnerExplodeListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBreak(EntityExplodeEvent e) {
        List<Block> broken = e.blockList();
            for (Block block : broken) {
                if (block.getType() != Material.MOB_SPAWNER) continue;
                if (AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(block.getLocation()) != null) {
                    if (CollectorManager.DELEGATE.isCollectorContained(block.getLocation())) {
                        Collector collector = CollectorManager.DELEGATE.getCollector(block.getLocation());
                        CreatureSpawner spawner = (CreatureSpawner) block.getState();
                        EntityType type = spawner.getSpawnedType();
                        collector.getSpawnersInChunk().remove(type);
                    }
                }
            }

    }
}