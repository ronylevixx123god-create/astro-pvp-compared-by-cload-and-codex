package net.astropvp.customblocks.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.FPlayers;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.CustomBlocksAPI;
import net.astropvp.customblocks.data.CustomBlockManager;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.LevelAble;
import net.astropvp.customblocks.struct.abilities.SellBot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockPlaceListener implements Listener {
    private CustomBlockManager customBlockManager;
    private CustomBlocks customBlocks;

    public CustomBlockPlaceListener(CustomBlocks plugin) {
        this.customBlocks = plugin;
        this.customBlockManager = plugin.getCustomBlockManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) throws IllegalAccessException, InstantiationException {
        ItemStack item = event.getItemInHand();
        if (event.isCancelled()) return;
        if (CustomBlocksAPI.isCustomBlockItem(item)) {
            CustomBlockType type = CustomBlocksAPI.getTypeFromItem(item);
            if (!FPlayers.getInstance().getByPlayer(event.getPlayer()).hasFaction()) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot place custom blocks without a faction (/f create <name>)"));
                event.setCancelled(true);
                return;
            }
            if (FactionUtils.isWilderness(event.getBlock().getLocation())) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot place custom blocks in wilderness!"));
                event.setCancelled(true);
                return;
            }
            if (!EconomyUtils.hasBalance(event.getPlayer(), type.getPlacePrice())) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You do not have enough money to place the " + type.getDisplayName() + " Custom Block!"));
                event.setCancelled(true);
                return;
            }

            if (CustomBlocksAPI.chunkContainsCustomBlock(event.getBlock().getLocation(), type)) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c This chunk already contains a " + type.getDisplayName() + " Custom Block!"));
                event.setCancelled(true);
                return;
            }


/*            if (CustomBlocksAPI.getFactionCustomBlocksOfType(FactionUtils.getFactionId(event.getPlayer()), type) >= CustomBlocks.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("maxCustomBlocksPerTypeFaction") * FactionUtils.getFaction(event.getPlayer()).getFPlayers().size()) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!)&c This chunk already contains a " + type.getDisplayName() + " Custom Block!"));
                event.setCancelled(true);
                return;
            }*/

            event.getPlayer().sendMessage(CC.translate("&c&l- $" + NumberUtils.formatMoney(type.getPlacePrice())));
            EconomyUtils.withdrawBalance(event.getPlayer(), type.getPlacePrice());

            CustomBlock customBlock = type.getCustomBlockClass().newInstance();
            customBlock.setLocation(event.getBlock().getLocation());

            if (customBlock instanceof LevelAble) {
                ((LevelAble) customBlock).setLevel(CustomBlocksAPI.getLevelFromItem(item));
            }
            customBlockManager.addToCache(customBlock);

            if(customBlock instanceof SellBot) {
                if (CollectorManager.DELEGATE.isCollectorContained(event.getBlock().getLocation().getChunk())) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(event.getBlock().getLocation());
                    collector.addBooster(((SellBot)customBlock).getEntityType());
                }
            }
        }
    }
}
