package me.venir.astrocollectors.listeners.spawnerlisteners;

import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnerBlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBreak(BlockBreakEvent e) {
            if (AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(e.getBlock().getLocation()) != null) {
                if (CollectorManager.DELEGATE.isCollectorContained(e.getBlock().getLocation())) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(e.getBlock().getLocation());
                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    EntityType type = spawner.getSpawnedType();
                    collector.getSpawnersInChunk().remove(type);
                }
            }
        }
    }
