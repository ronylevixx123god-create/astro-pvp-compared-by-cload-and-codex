/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.MapMaker
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.WorldGuardUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.listeners;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.MapMaker;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpaceFlareListener
implements Listener {
    private static HashSet<Location> spaceFlares;
    //private Map<UUID, Long> failedSpaceFlareAttempts = new MapMaker().expireAfterWrite(30L, TimeUnit.SECONDS).makeMap();
    private Cache<UUID, Long> failedSpaceFlareAttempts = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build();

    public SpaceFlareListener() {
        spaceFlares = new HashSet();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            boolean dropped;
            ItemStack is = e.getItem();
            if (!CosmicSpaceChestsAPI.isSpaceFlare(is)) {
                return;
            }
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            if (e.getPlayer().getWorld().getName().equals("world_arena")) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You can't use flares in the Battlefield.");
                return;
            }
            if (e.getPlayer().getWorld().getName().equals("world_nether")) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You can't use flares in the nether.");
                return;
            }
            if (e.getPlayer().getWorld().getName().equals("world_duels") || e.getPlayer().getWorld().getName().equals("world_duels2")) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You cannot redeem this item while in a duel.");
                return;
            }
            Player p = e.getPlayer();
            Long expiration = this.failedSpaceFlareAttempts.asMap().get(p.getUniqueId());
            if (expiration != null && expiration > System.currentTimeMillis()) {
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait " + CC.RedB + TimeUtils.formatFutureTime((long)expiration) + CC.Red + " before attempting to use another Space Flare!");
                return;
            }
            FLocation fL = new FLocation(p.getLocation());
            if (!Board.getInstance().getFactionAt((FLocation)fL).isWarZone() || WorldGuardUtils.isPvPDisabled((Location)p.getLocation())) {
                p.sendMessage(ChatColor.RED.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You " + (Object)ChatColor.UNDERLINE + "cannot" + (Object)ChatColor.RED + " deploy a Space Flare outside the Warzone!");
                return;
            }
            int chestAmount = is.getAmount();
            ItemRarity ir = CosmicSpaceChestsAPI.getItemRarityByColor(is);
            ChatColor cc = CosmicSpaceChestsAPI.getColor(ir);
            int attempts = 0;
            long time = System.currentTimeMillis();
            Location chestLoc = this.getRandomNearby(p);
            boolean bl = dropped = chestLoc != null && CosmicSpaceChestsAPI.dropChestSpaceFlare(chestLoc, ir, chestAmount);
            while (!dropped && attempts < 10) {
                chestLoc = this.getRandomNearby(p);
                if (chestLoc == null) {
                    ++attempts;
                    continue;
                }
                dropped = CosmicSpaceChestsAPI.dropChestSpaceFlare(chestLoc, ir, chestAmount);
                if (dropped) break;
                ++attempts;
            }
            Bukkit.getLogger().info("Took " + (System.currentTimeMillis() - time) + "ms to find chest location, dropped: " + dropped + " chest Location: " + (Object)chestLoc);
            if (!dropped) {
                this.failedSpaceFlareAttempts.put(p.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30L));
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "Unable to transmit safe loot cache coordinates, please try again in a safer area!");
                return;
            }
            p.setItemInHand(null);
            p.sendMessage((Object)cc + ChatColor.BOLD.toString() + "** " + ir.name + " Space Flare **");
            p.sendMessage((Object)ChatColor.GRAY + "Your request for " + (Object)cc + chestAmount + "x Loot Cache(s)" + (Object)ChatColor.GRAY + " has been received.");
            p.sendMessage((Object)ChatColor.GRAY + "Transmitting coordinates..... " + (Object)cc + "x:" + chestLoc.getBlockX() + "  y:" + chestLoc.getBlockY() + "  z:" + chestLoc.getBlockZ());
            for (Entity ent : p.getNearbyEntities(32.0, 32.0, 32.0)) {
                if (!(ent instanceof Player)) continue;
                ((Player)ent).sendMessage(ChatColor.DARK_AQUA.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.DARK_AQUA + "A " + (Object)cc + (Object)ChatColor.UNDERLINE + ir.name + " Space Flare" + (Object)ChatColor.DARK_AQUA + " has been launched nearby!");
            }
            Bukkit.getLogger().info("(CosmicSpaceChests) Summoning " + chestAmount + "x flare RARITY: " + ir.name + " for " + p.getName() + " near: " + fL.getX() + " z" + fL.getZ());
        }
    }

    private Location getRandomNearby(Player p) {
        return LocationUtils.getNearbyLocation(p.getLocation(), 8, 4, p.getWorld().getEnvironment() == World.Environment.NETHER ? 2 : 30);
    }

    public static HashSet<Location> getSpaceFlares() {
        return spaceFlares;
    }
}

