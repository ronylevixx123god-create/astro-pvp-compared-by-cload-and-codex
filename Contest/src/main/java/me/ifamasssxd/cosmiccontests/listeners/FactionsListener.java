/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.event.FPlayerJoinEvent
 *  com.massivecraft.factions.event.FPlayerLeaveEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmiccontests.listeners;

import com.compilex.CompilexPermissions.PermissionsAPI;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class FactionsListener
implements Listener {
    @EventHandler
    public void onPlayerLeave(FPlayerJoinEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicContests.get(), () -> {
            ContestUser user;
            if (event.getFPlayer() != null && event.getFPlayer().hasFaction() && (user = ContestUser.getUser(event.getFPlayer().getCachedUUID())) != null) {
                if (event.getFaction().isNormal()) {
                    user.setFactionTag(event.getFaction().getTag());
                } else {
                    user.setFactionTag(null);
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerLeave(FPlayerLeaveEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicContests.get(), () -> {
            ContestUser user;
            if (event.getFPlayer() != null && (user = ContestUser.getUser(event.getFPlayer().getCachedUUID())) != null) {
                user.setFactionTag(null);
            }
        }, 1L);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        FPlayer player = (FPlayer)FPlayers.i.get((OfflinePlayer)event.getPlayer());
        ContestUser user = ContestUser.getUser(event.getPlayer().getUniqueId());
        if (player != null && player.hasFaction() && player.getFaction().isNormal() && user != null) {
            user.setFactionTag(player.getFaction().getTag());
            return;
        }
        if (user != null) {
            user.setFactionTag(null);
        }

//        event.getPlayer().setPlayerListName(PermissionsAPI.getPermissionUser(event.getPlayer().getUniqueId()).getPrefix() + " " + event.getPlayer().getName());
    }
}

