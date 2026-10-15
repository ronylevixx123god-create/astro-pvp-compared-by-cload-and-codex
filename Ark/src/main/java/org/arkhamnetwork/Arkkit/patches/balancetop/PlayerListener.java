/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.ess3.api.events.UserBalanceUpdateEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package org.arkhamnetwork.Arkkit.patches.balancetop;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener
        implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        double cash = BalanceTop.getEconomy().getBalance(e.getPlayer());
        if (cash <= 0.0) {
            cash = 0.0;
        }
        if (!BalanceManager.balanceMap.containsKey(e.getPlayer().getUniqueId())) {
            BalanceManager.balanceMap.put(e.getPlayer().getUniqueId(), new NameDouble(e.getPlayer().getName(), cash));
        } else {
            NameDouble name = BalanceManager.balanceMap.get(e.getPlayer().getUniqueId());
            if (!e.getPlayer().getName().equals(name.getName())) {
                name.setName(e.getPlayer().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBalanceChangeEvent(UserBalanceUpdateEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getPlayer().getName().startsWith("faction_")) {
            if (BalanceManager.balanceMap.remove(event.getPlayer().getUniqueId()) != null) {
                Bukkit.getLogger().info("Removing stored balance for non player: " + event.getPlayer().getName() + ", how???? player: " + event.getPlayer());
            }
            return;
        }
        NameDouble current = BalanceManager.balanceMap.get(event.getPlayer().getUniqueId());
        if (current == null) {
            BalanceManager.balanceMap.put(event.getPlayer().getUniqueId(), new NameDouble(event.getPlayer().getName(), event.getNewBalance() == null ? 0.0 : event.getNewBalance().doubleValue()));
        } else {
            current.setAmount(event.getNewBalance() == null ? 0.0 : event.getNewBalance().doubleValue());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        double cash = Math.max(0.0, BalanceTop.getEconomy().getBalance(e.getPlayer()));
        BalanceManager.setBalance(e.getPlayer().getUniqueId(), e.getPlayer().getName(), cash);
    }
}

