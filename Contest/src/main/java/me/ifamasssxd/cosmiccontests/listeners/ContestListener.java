/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.nossr50.mcMMO
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.event.block.BlockPistonExtendEvent
 *  org.bukkit.event.block.BlockPistonRetractEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerFishEvent$State
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmiccontests.listeners;

import com.gmail.nossr50.mcMMO;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.utils.KillTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class ContestListener
implements Listener {
    private List<Material> placeable = Lists.newArrayList(Material.LOG, Material.LOG_2);
    private Set<Location> handPlaces = new HashSet<Location>();
    private Map<UUID, Set<UUID>> uniqueKills = new HashMap<UUID, Set<UUID>>();
    private Map<UUID, KillTracker> killTrackerMap = new HashMap<UUID, KillTracker>();

    @EventHandler
    public void onPlayerKillPlayer(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            Set<UUID> kills;
            ContestUser user = null;
            if (event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
                user = ContestUser.getUser(killer.getUniqueId());
                KillTracker tracker = this.killTrackerMap.computeIfAbsent(killer.getUniqueId(), t -> new KillTracker());
                Long lastTimeKilled = (Long)tracker.get(event.getEntity().getUniqueId());
                if (lastTimeKilled != null && System.currentTimeMillis() - lastTimeKilled <= TimeUnit.HOURS.toMillis(3L)) {
                    Bukkit.getLogger().info("Not counting kill on " + event.getEntity().getName() + " from " + killer.getName() + " due to being killed < 3 hours ago.");
                    return;
                }
                tracker.put(event.getEntity().getUniqueId(), System.currentTimeMillis());
                if (user != null) {
                    user.incrementValue(Contests.KILLS_IN_END, 1);
                }
            }
            if ((kills = this.uniqueKills.get(killer.getUniqueId())) != null && kills.contains(event.getEntity().getUniqueId())) {
                return;
            }
            if (kills == null) {
                kills = new HashSet<UUID>();
                this.uniqueKills.put(killer.getUniqueId(), kills);
            }
            kills.add(event.getEntity().getUniqueId());
            if (user == null) {
                user = ContestUser.getUser(killer.getUniqueId());
            }
            if (user != null) {
                user.incrementValue(Contests.UNIQUE_KILLS, 1);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerPlace(BlockPlaceEvent e) {
        if (e.getItemInHand() == null) {
            return;
        }
        Material material = e.getItemInHand().getType();
        if (this.placeable.contains((Object)material) || material.name().endsWith("_ORE")) {
            this.handPlaces.add(e.getBlock().getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonMove(BlockPistonExtendEvent e) {
        List<Block> blocks = Lists.newArrayList(e.getBlocks());
        for (Block b : blocks)
            this.handPlaces.add(b.getRelative(e.getDirection()).getLocation());
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPistonRetract(BlockPistonRetractEvent e) {
        if (this.handPlaces.contains((Object)e.getBlock().getLocation()) || this.handPlaces.contains((Object)e.getRetractLocation())) {
            this.handPlaces.add(e.getBlock().getRelative(e.getDirection()).getLocation());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerBreak(BlockBreakEvent event) {
        this.handPlaces.remove((Object)event.getBlock().getLocation());
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ContestUser user = ContestUser.getUser(player.getUniqueId());
        if (user != null) {
            for (Contests contest : CosmicContests.get().getContestManager().getActiveContests()) {
                BlockContest cont;
                if (!(contest.getContest() instanceof BlockContest) || (cont = (BlockContest)contest.getContest()).getBlockEventType() != BlockEventType.BREAK || !cont.isBlockApplicable(event.getBlock()) || mcMMO.getPlaceStore().isTrue(event.getBlock()) || this.handPlaces.contains((Object)event.getBlock().getLocation())) continue;
                cont.onBlockEvent(user, (BlockEvent)event);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            ContestUser user = ContestUser.getUser(killer.getUniqueId());
            if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                for (Contests active : CosmicContests.get().getContestManager().getActiveContests()) {
                    EntityKillContest found;
                    if (!(active.getContest() instanceof EntityKillContest) || (found = (EntityKillContest)active.getContest()).getEntityType() != null && found.getEntityType() != EntityType.UNKNOWN && found.getEntityType() != event.getEntityType()) continue;
                    found.onEntityKill(user, event);
                }
                if (event.getEntity().getCustomName() != null && ChatColor.stripColor((String)event.getEntity().getCustomName()).contains("Soul Demon")) {
                    user.incrementValue(Contests.SOUL_DEMONS_KILLED, 1);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerFish(PlayerFishEvent event) {
        Item item;
        ItemStack fish;
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item && (fish = (item = (Item)event.getCaught()).getItemStack()).getType() == Material.RAW_FISH) {
            ContestUser user = ContestUser.getUser(event.getPlayer().getUniqueId());
            user.incrementValue(Contests.FISH_CAUGHT, 1);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPLayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getItemInHand() != null && event.getItemInHand().getType() == Material.FLINT_AND_STEEL) {
            return;
        }
        ContestUser user = ContestUser.getUser(player.getUniqueId());
        if (user != null) {
            for (Contests contest : CosmicContests.get().getContestManager().getActiveContests()) {
                BlockContest cont;
                if (!(contest.getContest() instanceof BlockContest) || (cont = (BlockContest)contest.getContest()).getBlockEventType() != BlockEventType.PLACE || !cont.isBlockApplicable(event.getBlock())) continue;
                cont.onBlockEvent(user, (BlockEvent)event);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player damager;
        ContestUser user;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && (user = ContestUser.getUser((damager = (Player)event.getDamager()).getUniqueId())) != null) {
            user.incrementValue(Contests.DAMAGE_DEALT, event.getFinalDamage());
        }
    }
}

