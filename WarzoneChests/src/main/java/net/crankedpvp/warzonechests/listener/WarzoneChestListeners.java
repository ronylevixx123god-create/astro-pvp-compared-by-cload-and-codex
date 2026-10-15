package net.crankedpvp.warzonechests.listener;

import net.crankedpvp.warzonechests.WarzoneChestsPlugin;
import net.crankedpvp.warzonechests.struct.WarzoneChestManager;
import net.crankedpvp.warzonechests.struct.items.WarzoneChestPlacerItem;
import net.crankedpvp.warzonechests.utils.LocationSerializerDeserializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WarzoneChestListeners implements Listener {

    private final WarzoneChestManager warzoneChestManager;
    private final WarzoneChestsPlugin plugin;

    public WarzoneChestListeners(WarzoneChestsPlugin plugin) {
        this.warzoneChestManager = plugin.getWarzoneChestManager();
        this.plugin = plugin;
    }

    @EventHandler
    public void warzoneChestPlaceListener(BlockPlaceEvent event) {
        if (!WarzoneChestPlacerItem.isWarzoneChestPlace(event.getItemInHand())) return;

        this.warzoneChestManager.placeWarzoneChest(event.getBlockPlaced().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void warzoneChestBreakListener(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (event.getBlock().getType() != Material.CHEST) return;
        if (!this.warzoneChestManager.contains(LocationSerializerDeserializer.serialize(event.getBlock().getLocation()))) return;

        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }

    @EventHandler
    public void warzoneChestInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;
        if (!this.warzoneChestManager.contains(LocationSerializerDeserializer.serialize(event.getClickedBlock().getLocation()))) return;

        event.setCancelled(true);
        this.warzoneChestManager.getFromCache(LocationSerializerDeserializer.serialize(event.getClickedBlock().getLocation()))
                .openChest(event.getPlayer());
    }
}
