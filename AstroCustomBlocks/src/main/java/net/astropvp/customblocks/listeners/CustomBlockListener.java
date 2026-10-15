package net.astropvp.customblocks.listeners;

import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.data.CustomBlockManager;
import net.astropvp.customblocks.struct.CustomBlock;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomBlockListener implements Listener {
    private CustomBlockManager customBlockManager;
    private CustomBlocks customBlocks;

    public CustomBlockListener(CustomBlocks plugin) {
        this.customBlocks = plugin;
        this.customBlockManager = plugin.getCustomBlockManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
            if (customBlockManager.getCustomBlockData(event.getClickedBlock().getLocation()) == null) return;
            if (!event.isCancelled()) {
                event.setCancelled(true);
                CustomBlock customBlock = customBlockManager.getCustomBlockData(event.getClickedBlock().getLocation());
                customBlock.onClick(event.getPlayer());
            }
        }
    }
}
