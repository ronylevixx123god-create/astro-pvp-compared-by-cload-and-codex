package org.arkhamnetwork.CosmicCustomTNT.listeners;

import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import org.arkhamnetwork.CosmicCustomTNT.CosmicCustomTNT;
import org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftTNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TNTListener
implements Listener {
    public static Map<Location, TNTTypes> tnt_tracker = new HashMap<>();
    private final List<Material> nonSolidBlocks;
    private boolean coreProtectEnabled = false;
    public static final Map<UUID, TNTTypes> customTnt = new HashMap<>();


    public TNTListener() {
        this.coreProtectEnabled = new File("plugins/CoreProtect.jar").exists();
        this.nonSolidBlocks = new ArrayList<Material>(Arrays.asList(Material.AIR, Material.SAND, Material.WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.STATIONARY_WATER));
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onTNTPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() == Material.TNT) {
            TNTTypes tnt_type = this.getTNTType(e.getItemInHand());
            if (tnt_type == null) {
                return;
            }
            tnt_tracker.put(e.getBlockPlaced().getLocation(), tnt_type);
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent e) {
        if (e.getItem().getType() == Material.TNT) {
            TNTTypes tnt_type = this.getTNTType(e.getItem());
            if (tnt_type == null) {
                return;
            }
            tnt_tracker.put(e.getBlock().getLocation(), tnt_type);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onTNTBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.TNT && tnt_tracker.containsKey((Object)e.getBlock().getLocation())) {
            e.setCancelled(true);
            Location loc = e.getBlock().getLocation();
            e.getBlock().setType(Material.AIR);
            e.getBlock().getWorld().dropItemNaturally(loc, TNTTypes.getTNTItem(tnt_tracker.get((Object)loc)));
            tnt_tracker.remove((Object)loc);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onExplosivePrime(ExplosionPrimeEvent e) {
        if (e.getEntity() != null && e.getEntity() instanceof TNTPrimed) {
            EntityTNTPrimed tntPrimed = ((CraftTNTPrimed)e.getEntity()).getHandle();
            TNTTypes tntType = tnt_tracker.get(e.getEntity().getLocation());
            if (tntType != null) {
                //e.getEntity().setMetadata("custom_tnt", (MetadataValue)new FixedMetadataValue((Plugin)CosmicCustomTNT.get(), (Object)tntType.name()));
                customTnt.put(e.getEntity().getUniqueId(), tntType);
                return;
            }
            Location loc = tntPrimed.sourceLoc;
            TNTTypes tnt_type = tnt_tracker.remove((Object)loc);
            if (tnt_type != null) {
                //e.getEntity().setMetadata("custom_tnt", (MetadataValue)new FixedMetadataValue((Plugin)CosmicCustomTNT.get(), (Object)tnt_type.name()));
                customTnt.put(e.getEntity().getUniqueId(), tnt_type);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTNTExplode(EntityExplodeEvent e) {
        if (e.getEntity() == null || e.getEntityType() != EntityType.PRIMED_TNT)
            return;
        TNTPrimed tnt = (TNTPrimed)e.getEntity();

        if (customTnt.containsKey(tnt.getUniqueId())) {
            TNTTypes tnt_type = customTnt.get(tnt.getUniqueId());


            switch (tnt_type) {
                case Lucky:
                    //Coded in AstroSpawners plugin
                case Huge:
                    Location loc = tnt.getLocation();
                    spawnFastExplosion(loc.clone().add(0.0D, 3.0D, 0.0D), true);
                    spawnFastExplosion(loc.clone().add(0.0D, -3.0D, 0.0D), true);
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        spawnFastExplosion(loc.clone().add(3.0D, 0.0D, 0.0D), true);
                        spawnFastExplosion(loc.clone().add(0.0D, 0.0D, 3.0D), true);
                    } else {
                        spawnFastExplosion(loc.clone().add(-3.0D, 0.0D, 0.0D), true);
                        spawnFastExplosion(loc.clone().add(0.0D, 0.0D, -3.0D), true);
                    }
                    break;
                case Lethal:
                    for (int i = 0; i < 4; i++)
                        spawnFastExplosion(tnt.getLocation());
                    break;
                case Mimic:
                    final Location l = tnt.getLocation();
                    Bukkit.getScheduler().runTaskLater((Plugin)CosmicCustomTNT.get(), new Runnable() {
                        public void run() {
                            TNTListener.this.spawnFastExplosion(l);
                        }
                    },100L);
                    Bukkit.getScheduler().runTaskLater((Plugin)CosmicCustomTNT.get(), new Runnable() {
                        public void run() {
                            TNTListener.this.spawnFastExplosion(l);
                        }
                    },200L);
                    Bukkit.getScheduler().runTaskLater((Plugin)CosmicCustomTNT.get(), new Runnable() {
                        public void run() {
                            TNTListener.this.spawnFastExplosion(l);
                        }
                    },300L);
                    break;
                default:
                    Bukkit.getLogger().info("[CosmicCustomCreeper] No Creeper type found!");

            }

        }
    }

    private TNTTypes getTNTType(ItemStack tnt_is) {
        if (!(tnt_is != null && tnt_is.hasItemMeta() && tnt_is.getItemMeta().hasDisplayName() && tnt_is.getItemMeta().hasLore())) {
            return null;
        }
        TNTTypes tnt_type = null;
        String name = tnt_is.getItemMeta().getDisplayName();
        for (TNTTypes tt : TNTTypes.values()) {
            if (!name.equals(tt.getItemName())) continue;
            tnt_type = tt;
            break;
        }
        return tnt_type;
    }

    private void spawnFastExplosion(Location l) {
        this.spawnFastExplosion(l, true);
    }

    private void spawnFastExplosion(Location l, boolean onlyAir) {
        Material type = l.getBlock().getType();
        if (onlyAir && type != Material.AIR || !this.nonSolidBlocks.contains((Object)type)) {
            return;
        }
        TNTPrimed tnt = (TNTPrimed)l.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
        tnt.setMetadata("fastExplosion", (MetadataValue)new FixedMetadataValue((Plugin)CosmicCustomTNT.get(), (Object)true));
        tnt.setFuseTicks(0);
    }

    private void logBreak(Block block, int id, byte data, String tnt) {
        if (!this.coreProtectEnabled) {
            return;
        }
        try {
            CoreProtectAPI api = ((CoreProtect)Bukkit.getPluginManager().getPlugin("CoreProtect")).getAPI();
            if (api != null) {
                api.logRemoval(tnt, block.getLocation(), id, data);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

