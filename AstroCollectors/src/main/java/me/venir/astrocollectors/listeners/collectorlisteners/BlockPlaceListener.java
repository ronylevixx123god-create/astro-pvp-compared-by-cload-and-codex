package me.venir.astrocollectors.listeners.collectorlisteners;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import me.venir.astrocollectors.collector.CollectorManager;
import me.venir.astrocollectors.collector.CollectorUtils;
import me.venir.astrocollectors.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onCollectorPlace(BlockPlaceEvent event) {
        if (!CollectorUtils.isCollector(event.getItemInHand())) return;
        if (event.isCancelled()) return;
        Location location = event.getBlockPlaced().getLocation();

        if (CollectorManager.DELEGATE.isCollectorContained(location)) {
            Language.CAN_NOT_PLACE_HERE.send(event.getPlayer());
            event.getPlayer().sendMessage(ChatColor.RED + "The collector is located at: " + CollectorManager.DELEGATE.getCollector(location.getChunk()).getLocation().getBlockX() + " "
            + CollectorManager.DELEGATE.getCollector(location.getChunk()).getLocation().getBlockY() + " " + CollectorManager.DELEGATE.getCollector(location.getChunk()).getLocation().getBlockZ());
            event.getPlayer().sendMessage(ChatColor.RED + "You can remove this chunk collector by typing: /removecollector");
            event.setCancelled(true);
            return;
        }

        FLocation fLocation = new FLocation(location);

        if (Board.getInstance().getFactionAt(fLocation).isWilderness()) {
            Language.CAN_NOT_PLACE_IN_WILD.send(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        CollectorManager.DELEGATE.newCollector(location);
        Language.PLACED_COLLECTOR.send(event.getPlayer());
    }
}
