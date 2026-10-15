/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.google.common.collect.Lists
 *  com.google.common.collect.MapMaker
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.Faction
 *  net.minecraft.util.io.netty.util.internal.ConcurrentSet
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.BrewingStand
 *  org.bukkit.block.Furnace
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.BrewEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.FurnaceBurnEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmichcf.listeners;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.util.internal.ConcurrentSet;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import me.ifamasssxd.cosmichcf.utils.BlockUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OutpostPerkListener
implements Listener {
    private Map<Faction, Set<Block>> cachedBlocksToBoost = new MapMaker().concurrencyLevel(32).makeMap();

    public OutpostPerkListener() {
        new BukkitRunnable(){

            public void run() {
                HashSet facsToRemove = new HashSet();
                OutpostPerkListener.this.cachedBlocksToBoost.forEach((fac, blocks) -> {
                    boolean capsGreatHall;
                    boolean capsCount = HCFOutpostAPI.isControllingOutpost(fac, OutpostType.COURTYARD);
                    boolean bl = capsGreatHall = !capsCount && HCFOutpostAPI.isControllingOutpost(fac, OutpostType.THE_GREAT_HALL);
                    if (!capsCount && !capsGreatHall) {
                        facsToRemove.add(fac);
                        return;
                    }
                    if (capsCount) {
                        ArrayList toRemove = Lists.newArrayList();
                        blocks.forEach(furnaceBlock -> {
                            if (!BlockUtils.isChunkLoaded(furnaceBlock)) {
                                return;
                            }
                            BlockState state = furnaceBlock.getState();
                            if (state instanceof Furnace) {
                                try {
                                    Furnace furn = (Furnace)state;
                                    if (furn.getBurnTime() > 1) {
                                        furn.setCookTime((short)(furn.getCookTime() + 17));
                                    }
                                }
                                catch (Exception e) {
                                    toRemove.add(furnaceBlock);
                                    Bukkit.getLogger().info("Problem processing furnace for " + fac.getTag());
                                    e.printStackTrace();
                                }
                            } else {
                                OutpostPerkListener.this.cachedBlocksToBoost.remove(fac, furnaceBlock);
                            }
                        });
                        toRemove.forEach(blocks::remove);
                    } else {
                        ArrayList toRemove = Lists.newArrayList();
                        blocks.forEach(furnaceBlock -> {
                            if (!BlockUtils.isChunkLoaded(furnaceBlock)) {
                                return;
                            }
                            BlockState state = furnaceBlock.getState();
                            if (state instanceof BrewingStand) {
                                try {
                                    BrewingStand brewing = (BrewingStand)state;
                                    if (brewing.getBrewingTime() > 0) {
                                        brewing.setBrewingTime(Math.max(1, brewing.getBrewingTime() - 15));
                                        brewing.update();
                                    } else if (brewing.getBrewingTime() < 15 && brewing.getBrewingTime() > 0) {
                                        brewing.setBrewingTime(0);
                                        brewing.update();
                                    }
                                }
                                catch (Exception e) {
                                    toRemove.add(furnaceBlock);
                                    Bukkit.getLogger().info("Problem processing brewingstand for " + fac.getTag());
                                    e.printStackTrace();
                                }
                            } else {
                                OutpostPerkListener.this.cachedBlocksToBoost.remove(fac, furnaceBlock);
                            }
                        });
                        toRemove.forEach(blocks::remove);
                    }
                });
                facsToRemove.forEach(facFound -> {
                    Set cfr_ignored_0 = (Set)OutpostPerkListener.this.cachedBlocksToBoost.remove(facFound);
                });
            }
        }.runTaskTimer(CosmicKOTHPlugin.get_(), 20L, 20L);
    }



    @EventHandler
    public void extraDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() == null) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        Player p = ((Player) e.getDamager());
        if (HCFOutpostAPI.isControllingOutpost(FactionUtils.getFaction(p), OutpostType.COURTYARD)) {
            if (p.getLocation().getWorld().getName().equals("tournament_world")) return;
            //e.setDamage(e.getDamage()*1.15);
        }
    }


    @EventHandler
    public void recudeDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() == null) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        Player p = ((Player) e.getEntity()).getPlayer();
        if (HCFOutpostAPI.isControllingOutpost(FactionUtils.getFaction(p), OutpostType.THE_GREAT_HALL)) {
            if (p.getLocation().getWorld().getName().equals("tournament_world")) return;
            //e.setDamage(e.getDamage()*0.85);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onSaturationLoss(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (event.getFoodLevel() < player.getFoodLevel() && Math.random() <= 0.35 && HCFOutpostAPI.isControllingOutpost(player, OutpostType.COURTYARD)) {
            player.setSaturation(player.getSaturation() + 0.5f);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Faction at;
        Block block;
        Material mat;
        if (event.hasBlock() && ((mat = (block = event.getClickedBlock()).getType()) == Material.FURNACE || mat == Material.BURNING_FURNACE || mat == Material.BREWING_STAND) && (at = FactionUtils.getFactionAt((Location)block.getLocation())) != null && at.isNormal() && (HCFOutpostAPI.isControllingOutpost(at, OutpostType.COURTYARD) && (mat == Material.BURNING_FURNACE || mat == Material.FURNACE) || HCFOutpostAPI.isControllingOutpost(at, OutpostType.THE_GREAT_HALL) && mat == Material.BREWING_STAND)) {
            this.cachedBlocksToBoost.computeIfAbsent(at, se -> new ConcurrentSet()).add(block);
        }
    }

    @EventHandler
    public void onBrewTrack(BrewEvent event) {
        Faction at;
        if (event.getBlock().getType() == Material.BREWING_STAND && this.cachedBlocksToBoost.values().stream().filter(set -> set.contains((Object)event.getBlock())).findFirst().orElse(null) != null && (at = FactionUtils.getFactionAt((Location)event.getBlock().getLocation())) != null && at.isNormal()) {
            this.cachedBlocksToBoost.computeIfAbsent(at, set -> new HashSet()).add(event.getBlock());
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        BlockState state = e.getBlock().getState();
        if (state instanceof Furnace) {
            Furnace furnace = (Furnace)state;
            FLocation fl = new FLocation(e.getBlock().getLocation());
            Board.getInstance();
            Faction f = FactionUtils.getFactionAt((FLocation)fl);
            if (f != null && f.isNormal() && furnace.getBurnTime() > 1 && HCFOutpostAPI.isControllingOutpost(f, OutpostType.COURTYARD)) {
                this.cachedBlocksToBoost.computeIfAbsent(f, se -> new ConcurrentSet()).add(e.getBlock());
            }
        } else if (e.getBlock().getType() != Material.AIR) {
            Bukkit.getLogger().info("[FactionUpgrades] Invalid FurnaceBurnEvent at " + (Object)e.getBlock().getLocation() + " TYPE " + e.getBlock().getType().name());
        }
    }
}

