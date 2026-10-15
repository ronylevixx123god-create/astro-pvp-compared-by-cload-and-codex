/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.controller;

import me.recolance.auction.Auction;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.data.DataHandler;
import me.recolance.auction.data.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Events
        implements Listener {
    public static Set<UUID> loadingUUIDs = new HashSet<UUID>();

    public static boolean isLoading(Player player) {
        return loadingUUIDs.contains(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        List<UnclaimedListing> found = DataHolder.getUnclaimedListings().get(uuid);
        if (found != null) {
            Bukkit.getLogger().info("[AuctionHouse] Skipping loadUnclaimedListings for " + uuid.toString() + " due to already having keys (" + found + ") in unclaimedListings map.");
            loadingUUIDs.remove(uuid);
            return;
        }
        if (loadingUUIDs.contains(player.getUniqueId())) {
            Bukkit.getLogger().info("[AuctionHouse] Skipping loading of data for " + player.getUniqueId() + " since there is a pending thread already handling it.");
            return;
        }
        loadingUUIDs.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously((Plugin) Auction.getPlugin(), () -> {
            DataHandler.loadUnclaimedListings(uuid);
            loadingUUIDs.remove(player.getUniqueId());
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        DataHolder.removeUnclaimedListingStore(event.getPlayer().getUniqueId());
    }
}

