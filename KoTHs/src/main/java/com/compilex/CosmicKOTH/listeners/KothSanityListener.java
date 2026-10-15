/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDamageEvent$DamageModifier
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicKOTH.listeners;

import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.utils.RewardUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ExpUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.LoadingCache;
import com.massivecraft.factions.Faction;
import com.sk89q.jchronic.utils.Time;
import net.astropvp.core.utils.DamageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class KothSanityListener implements Listener {
    private Map<UUID, Integer> storeEXP = new HashMap<>();

    private LoadingCache recentKothDeaths = CacheBuilder.newBuilder().expireAfterWrite(8L, TimeUnit.MINUTES).build((CacheLoader)new CacheLoader<UUID, Long>() {
        public Long load(UUID uuid) {
            return System.currentTimeMillis();
        }
    });

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() == null) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null) return;

        if (faction.getId().equals(CosmicKOTHPlugin.get_().getBuffFactionId())) {
            if (player.getLocation().getWorld().getName().equals("tournament_world")) return;
            //DamageUtils.setDamageIncrease(10, event);
        }
    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (CosmicKOTHPlugin.get_().getKoth() != null) {
            Player player = e.getPlayer();
            CosmicKOTHPlugin.get_().getKoth().getClass();
            player.removeMetadata("KOTH_CAPPING_START", (Plugin)CosmicKOTHPlugin.get_());
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/warp koth")) {
            e.setMessage("/koth warp");
        }
    }



    @EventHandler(priority=EventPriority.LOWEST)
    public void onPreviewInventoryClickLOWEST(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals("KOTH Loot Preview")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPreviewInventoryClickMONITOR(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals("KOTH Loot Preview")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onRespawnFromKothDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (e.getEntity().getWorld().getName().equals("world_koth") && this.storeEXP != null && this.storeEXP.containsKey(e.getEntity().getUniqueId())) {
            ExpUtils.setTotalExperience(player, this.storeEXP.get(player.getUniqueId()));
            this.storeEXP.remove(e.getEntity().getUniqueId());
            player.sendMessage(CC.translate((String)"&a&l(!) &aSince you died in the KOTH world, your inventory contents & EXP were saved!"));
        }
        if (e.getEntity().getWorld().getName().contains("_koth")) {
            this.recentKothDeaths.apply(e.getEntity().getUniqueId());
            CosmicKOTHAPI.playerLivesUsed.put(e.getEntity().getUniqueId(), CosmicKOTHAPI.playerLivesUsed.getOrDefault(e.getEntity().getUniqueId(), 0) + 1);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getFrom() != null && event.getTo() != null && event.getTo().getWorld().getName().contains("_koth") && !event.getTo().getWorld().getName().equals(event.getFrom().getWorld().getName())) {
            Long lastDeath;
            if (CosmicKOTHAPI.playerLivesUsed.containsKey(event.getPlayer().getUniqueId()) && CosmicKOTHAPI.playerLivesUsed.get(event.getPlayer().getUniqueId()) == 2) {
                event.getPlayer().sendMessage(CC.translate((String)"&c&l(!) &cYou cannot return to the KOTH world because you have used both of your lives!"));
                event.setCancelled(true);
                if (!event.getPlayer().isOp()) {
                    return;
                }
            }
            if ((lastDeath = (Long)this.recentKothDeaths.asMap().get(event.getPlayer().getUniqueId())) != null) {
                int secondsLeft;
                long timeToLookFor = this.getTimeTillReturn(event.getPlayer());
                long timeSinceDeath = System.currentTimeMillis() - lastDeath;
                if (timeSinceDeath < timeToLookFor && (secondsLeft = (int)((timeToLookFor - timeSinceDeath) / 1000L)) > 0) {
                    if (!event.getPlayer().isOp()) {
                        event.setCancelled(true);
                    }
                    event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot return to the KOTH world for another " + CC.RedU + TimeUtils.formatSeconds((long)secondsLeft) + CC.Red + " due to your recent death!");
                }
            }
        }
    }

    private long getTimeTillReturn(Player player) {
        return player.hasPermission("cosmicpvp.heroic") ? TimeUnit.MINUTES.toMillis(5L) : TimeUnit.MINUTES.toMillis(8L);
    }

}

