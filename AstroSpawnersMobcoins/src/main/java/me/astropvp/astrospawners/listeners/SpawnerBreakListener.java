package me.astropvp.astrospawners.listeners;

import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.compilex.CosmicCustomCreepers.listeners.CreeperListener;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.data.SpawnerData;
import me.astropvp.astrospawners.data.SpawnerManager;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.astropvp.astrospawners.utils.EntityUtils;
import me.astropvp.factionlogs.struct.impl.SpawnerBreak;
import me.astropvp.factionlogs.struct.impl.SpawnerPlace;
import me.astropvp.factionlogs.utils.FactionsLogsAPI;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes;
import org.arkhamnetwork.CosmicCustomTNT.listeners.TNTListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class SpawnerBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        double chance = event.getEntity() instanceof TNTPrimed ? 0.20 : event.getEntity() instanceof Creeper ? 0.30 : 0;
        if (chance <= 0) {
            return;
        }
        if (!(event.getEntity() instanceof Creeper) && !(event.getEntity() instanceof TNTPrimed)) {
            return;
        }

        if (event.getEntity() instanceof Creeper) {

            Creeper c = (Creeper)event.getEntity();

            if (CreeperListener.creepers.containsKey(c.getUniqueId())) {

                CreeperTypes egg_type = CreeperListener.creepers.get(c.getUniqueId());
                if (egg_type == CreeperTypes.Raid) {
                    chance = 1;
                } else
                if (egg_type == CreeperTypes.Lucky) {
                    chance += 0.3;
                }
            }
        }
        if(event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
            if(TNTListener.customTnt.containsKey(tnt.getUniqueId())) {

                TNTTypes tntType = TNTListener.customTnt.get(tnt.getUniqueId());

                if(tntType == TNTTypes.Lucky)
                    chance += 0.35;
            }
        }
        List<Block> blocks = event.blockList();
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            if (block.getType() == Material.MOB_SPAWNER) {
                SpawnerData data = AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(block.getLocation());
                if (data != null) {
                    EntityType type = data.getEntityType();
                    int amount = data.getSpawnerAmount();
                    World world = block.getWorld();
                    Location location = block.getLocation();
                    int spawnerAmount = 0;
                    for(int pos = 0; pos < amount; pos++) {
                        if(chance > Math.random()) {
                            spawnerAmount++;
                        }
                    }
                    if (spawnerAmount > 0) {
                        world.dropItem(location, ItemSpawner.getItem(type, spawnerAmount));
                    }
                    data.setSpawnerAmount(0);
                    AstroSpawners.getInstance().getSpawnerManager().removeFromCache(data);
                    if (CollectorManager.DELEGATE.isCollectorContained(block.getLocation())) {
                        Collector collector = CollectorManager.DELEGATE.getCollector(block.getLocation());
                        collector.clearSpawners();
                        collector.scanSpawners(collector.getLocation());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMine(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.MOB_SPAWNER) {
            Block block = event.getBlock();
            SpawnerData data = AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(block.getLocation());
            if (data != null) {
                EntityType type = data.getEntityType();
                int amount = data.getSpawnerAmount();
                block.getWorld().dropItem(block.getLocation(), ItemSpawner.getItem(type, amount));
                data.setSpawnerAmount(0);
                event.getPlayer().sendMessage(CC.translate("&c&l- &c&n" + amount + "&c " + EntityUtils.getEntityName(type) + " Spawner(s)"));
                AstroSpawners.getInstance().getSpawnerManager().removeFromCache(data);
                block.setType(Material.AIR);
                event.setCancelled(true);
                if (CollectorManager.DELEGATE.isCollectorContained(event.getBlock().getLocation())) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(event.getBlock().getLocation());
                    collector.clearSpawners();
                    collector.scanSpawners(collector.getLocation());
                }
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
                if (!fPlayer.getFaction().isWilderness()) {
                    FactionsLogsAPI.log(fPlayer.getFaction().getId(), new SpawnerBreak(amount, event.getBlock().getLocation(), type, fPlayer.getPlayer().getUniqueId()));
                }
            }
        }
    }
}
