package net.astropvp.customblocks.listeners;

import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.CustomBlocksAPI;
import net.astropvp.customblocks.data.CustomBlockManager;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.LevelAble;
import net.astropvp.customblocks.struct.abilities.SellBot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class CustomBlockBreakListener implements Listener {
    private CustomBlockManager customBlockManager;
    private CustomBlocks customBlocks;

    public CustomBlockBreakListener(CustomBlocks plugin) {
        this.customBlocks = plugin;
        this.customBlockManager = plugin.getCustomBlockManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        //if (event.isCancelled()) return;
        if (CustomBlocksAPI.isCustomBlock(event.getBlock().getLocation())) {
            CustomBlock customBlockData = CustomBlocksAPI.getCustomBlockData(event.getBlock().getLocation());
            CustomBlockType type = customBlockData.getType();

            /*
            if (!EconomyUtils.hasBalance(event.getPlayer(), type.getMinePrice())) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You do not have enough money to mine the " + type.getDisplayName() + " Custom Block!"));
                event.setCancelled(true);
                return;
            }

             */
            int level = 0;
            if (customBlockData instanceof LevelAble) {
                level = ((LevelAble) customBlockData).getLevel();
            }
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), customBlockData.getType().build(level));
            //EconomyUtils.withdrawBalance(event.getPlayer(), type.getMinePrice());
            //event.getPlayer().sendMessage(CC.translate("&c&l- $" + NumberUtils.formatMoney(type.getMinePrice())));
            customBlockManager.removeFromCache(customBlockData);

            if(customBlockData instanceof SellBot) {
                if (CollectorManager.DELEGATE.isCollectorContained(event.getBlock().getLocation().getChunk())) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(event.getBlock().getLocation());
                    collector.removeBooster(((SellBot)customBlockData).getEntityType());
                }
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        for (Block block : blockList) {
            if (block.getType() != Material.NOTE_BLOCK) continue;
            if (CustomBlocksAPI.isCustomBlock(block.getLocation())) {
                block.getDrops().clear();
                CustomBlock customBlockData = CustomBlocksAPI.getCustomBlockData(block.getLocation());
                block.setType(Material.AIR);
                int level = 0;
                if (customBlockData instanceof LevelAble) {
                    level = ((LevelAble) customBlockData).getLevel();
                }
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), customBlockData.getType().build(level));
                customBlockManager.removeFromCache(customBlockData);
            }
        }
    }
}
