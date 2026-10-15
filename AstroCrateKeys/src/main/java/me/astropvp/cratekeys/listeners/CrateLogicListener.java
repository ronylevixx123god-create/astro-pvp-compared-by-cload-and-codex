package me.astropvp.cratekeys.listeners;

import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.AstroCrateKeysPlugin;
import me.astropvp.cratekeys.data.CrateData;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.inventory.LootPreviewGUI;
import me.astropvp.cratekeys.items.CratePlacementItem;
import me.astropvp.cratekeys.managers.CrateDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CrateLogicListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        CrateType type = CratePlacementItem.getCrateType(item);
        if (type == null)
            return;
        final Block block = event.getBlock();
        CrateDataManager.newInstance(type, block.getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Chest))
            return;
        CrateData data = AstroCrateKeysAPI.findCrate(block.getLocation());
        if (data == null)
            return;
        CrateDataManager.removeInstance(data);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        CrateData data = AstroCrateKeysAPI.findCrate(block.getLocation());
        if (data == null)
            return;

        if (player.isSneaking() && player.isOp()) return;

        event.setCancelled(true);

        LootPreviewGUI lootPreviewGUI = new LootPreviewGUI(player, data.getCrateType());
        lootPreviewGUI.getLootGUI().open(player);
    }

}
