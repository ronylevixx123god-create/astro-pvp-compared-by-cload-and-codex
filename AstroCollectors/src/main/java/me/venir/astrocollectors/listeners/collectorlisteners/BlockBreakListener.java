package me.venir.astrocollectors.listeners.collectorlisteners;

import me.venir.astrocollectors.AstroCollectors;
import me.venir.astrocollectors.collector.CollectorManager;
import me.venir.astrocollectors.collector.CollectorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreakListener implements Listener {

    private final AstroCollectors plugin;

    public BlockBreakListener(AstroCollectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        removeCollector(event.getBlock());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if (event.isCancelled()) return;
        for (Block block : event.blockList()) {
            removeCollector(block);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;
        for (Block block : event.blockList()) {
            removeCollector(block);
        }
    }

    @EventHandler
    public void onEntityExplode(BlockExplodeEvent event) {
        if (event.isCancelled()) return;
        for (Block block : event.blockList()) {
            removeCollector(block);
        }
    }

    private void removeCollector(Block block) {
        if (block.getType() != Material.BEACON) return;
        Location location = block.getLocation();
        if (!CollectorManager.DELEGATE.isCollectorContained(location)) return;
        Location collectorLocation = CollectorManager.DELEGATE.getCollector(location).getLocation();
        assert collectorLocation != null;
        CollectorManager.DELEGATE.getCollector(location).deleteHologram();
        if (!collectorLocation.equals(location)) return;
        CollectorManager.DELEGATE.removeCollector(location);
        block.setType(Material.AIR,true);
        new BukkitRunnable() {
            @Override public void run() {
                block.getWorld().dropItem(location, CollectorUtils.createCollector(1));
            }
        }.runTask(plugin);
    }
}
