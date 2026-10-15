/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.earth2me.essentials.User
 *  com.google.common.collect.Lists
 *  com.google.common.collect.MapMaker
 *  org.arkhamnetwork.Arkkit.patches.CombatLog
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.EssentialsUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmichcf.listeners;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.earth2me.essentials.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.Outpost;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class OutpostWorldListener
implements Listener {
    private List<BlockFace> faces = Lists.newArrayList(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
    private Location safeOutsideElevator = new Location(null, 0.0, 70.0, 0.0);
    //private Map<UUID, Long> main = new MapMaker().expireAfterWrite(15L, TimeUnit.SECONDS).makeMap();
    private ConcurrentMap<Object, Object> main = CacheBuilder.newBuilder().expireAfterWrite(15L, TimeUnit.SECONDS).build().asMap();

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Set regionsInside = WorldGuardUtils.getRegionsIn((Location)player.getLocation());
        CosmicKOTHPlugin.get_().getOutpostManager().onPlayerJoin(event, regionsInside);
        if (regionsInside != null) {
            for (Outpost outpost : CosmicKOTHPlugin.get_().getOutpostManager().getOutposts()) {
                if (!outpost.isInsideSellRoom(player, regionsInside)) continue;
                Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicKOTHPlugin.get_(), () -> {
                    Location loc;
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "You seem to have logged in inside the sell room, you have been moved!");
                    Outpost post = CosmicKOTHPlugin.get_().getOutpostManager().getOutpostInside(player.getLocation());
                    if (post != null && post.getTeleportLocation() != null) {
                        loc = post.getTeleportLocation().clone();
                    } else {
                        loc = this.safeOutsideElevator.clone();
                        loc.setWorld(player.getWorld());
                    }
                    player.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue(CosmicKOTHPlugin.get_(), (Object)""));
                    player.teleport(loc, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                    player.removeMetadata("ignore_combat_tag", CosmicKOTHPlugin.get_());
                    Bukkit.getLogger().info("(CosmicOutposts) " + player.getName() + " was found inside sell room on login: " + (Object)loc);
                }, 1L);
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        HCFOutpostAPI.clear(event.getPlayer());
        CosmicKOTHPlugin.get_().getOutpostManager().onPlayerLeave(event);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Set regions = WorldGuardUtils.getRegionsIn((Location)event.getEntity().getLocation());
        if (regions != null && EssentialsUtils.getEssentials() != null) {
            for (Outpost post : CosmicKOTHPlugin.get_().getOutpostManager().getOutposts()) {
                if (!post.isInsideSellRoom(event.getEntity(), regions)) continue;
                if (this.clearBackLocaiton(event.getEntity())) {
                    this.main.put(event.getEntity().getUniqueId(), System.currentTimeMillis());
                }
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Long timer = (Long) this.main.remove(event.getPlayer().getUniqueId());
        if (timer != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicKOTHPlugin.get_(), () -> {
                if (this.clearBackLocaiton(event.getPlayer())) {
                    Bukkit.getLogger().info("Clearing /back location for " + event.getPlayer().getName());
                } else {
                    Bukkit.getLogger().info("Unable to clear /back location for " + event.getPlayer().getName() + "!");
                }
            }, 1L);
        }
    }

    private boolean clearBackLocaiton(Player player) {
        User user = EssentialsUtils.getEssentials().getUser(player);
        if (user != null) {
            user.setLastLocation();
            return true;
        }
        return false;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        block12: {
            List val;
            Material material;
            Block clicked;
            block13: {
                boolean sellRoomSign;
                if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !event.hasBlock()) break block12;
                clicked = event.getClickedBlock();
                Material material2 = material = clicked != null ? clicked.getType() : null;
                if (clicked == null || material != Material.WALL_SIGN && material != Material.SIGN_POST) break block13;
                Sign sign = (Sign)clicked.getState();
                String[] lines = sign.getLines();
                if (lines == null || lines.length <= 1) break block12;
                String line = ChatColor.stripColor((String)lines[0]);
                boolean elevator = line.contains("[Elevator]");
                boolean bl = sellRoomSign = !elevator && line.contains("[Sell Room]");
                if (!elevator && !sellRoomSign) break block12;
                Set regions = WorldGuardUtils.getRegionsIn((Location)clicked.getLocation());
                Outpost inside = CosmicKOTHPlugin.get_().getOutpostManager().getOutpostInside(regions);
                if (inside == null) break block12;
                if (CombatLog.inCombat((Player)event.getPlayer())) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    if (elevator) {
                        event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot use an " + CosmicKOTHPlugin.get_().getBaseName() + " elevator while in combat!");
                    } else {
                        event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot be teleported to the Sell Room while in combat!");
                    }
                    return;
                }
                String id = FactionUtils.getFactionId((Player)event.getPlayer());
                if (inside.getLastFactionToCap() == null || id == null || !id.equals(inside.getLastFactionToCap())) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    if (elevator) {
                        event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must have control of the " + CosmicKOTHPlugin.get_().getBaseName() + " to use this Elevator!");
                    } else {
                        event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must have control of the " + CosmicKOTHPlugin.get_().getBaseName() + " to use this Sell Room teleport sign!");
                    }
                    return;
                }
                if (!sellRoomSign) break block12;
                if (inside.isInsideSellRoom(event.getPlayer(), regions)) {
                    event.getPlayer().teleport(inside.getTeleportLocation());
                    Bukkit.getLogger().info("[CosmicHCFOutposts] Teleporting " + event.getPlayer().getName() + " to teleport location for " + inside.getOutpostName() + " due to being inside sell room!");
                } else if (inside.getSellRoomLocation() != null) {
                    event.getPlayer().teleport(inside.getSellRoomLocation());
                } else {
                    event.getPlayer().sendMessage(CC.Red + "No Sell Room Location set for this " + CosmicKOTHPlugin.get_().getBaseName() + "!");
                }
                break block12;
            }
            if ((material == Material.CHEST || material == Material.TRAPPED_CHEST) && (val = clicked.getMetadata("strongholdChest")) != null && !val.isEmpty() && ((MetadataValue)val.get(0)).asBoolean()) {
                clicked.setMetadata("strongholdChest", (MetadataValue)new FixedMetadataValue(CosmicKOTHPlugin.get_(), (Object)false));
                Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(event.getPlayer(), "STRONGHOLD_LOOTER", new Object[]{clicked}));
                for (BlockFace face : this.faces) {
                    Block block = clicked.getRelative(face);
                    if (!block.hasMetadata("strongholdChest")) continue;
                    block.setMetadata("strongholdChest", (MetadataValue)new FixedMetadataValue(CosmicKOTHPlugin.get_(), (Object)false));
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
    public void onBlockBreak(BlockBreakEvent event) {
        CosmicKOTHPlugin.get_().getOutpostManager().onBlockBreak(event);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
    public void onBlockPlace(BlockPlaceEvent event) {
        CosmicKOTHPlugin.get_().getOutpostManager().onBlockPlace(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockPlaceMONITOR(BlockPlaceEvent event) {
        CosmicKOTHPlugin.get_().getOutpostManager().onBlockPlaceMONITOR(event);
    }

}

