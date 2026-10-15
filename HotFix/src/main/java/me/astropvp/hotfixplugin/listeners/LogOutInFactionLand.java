package me.astropvp.hotfixplugin.listeners;


import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import me.astropvp.hotfixplugin.HotFixPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogOutInFactionLand implements Listener {
    Map<UUID, Location> logouts;
    public LogOutInFactionLand() {
        this.logouts = new HashMap<>();
    }

    @EventHandler
    public void onLogOut(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!FactionUtils.isInsideOwnFaction(player) && !FactionUtils.isWarZone(player.getLocation()) && !FactionUtils.isWilderness(player.getLocation())) {
            logouts.put(player.getUniqueId(), player.getLocation());
            Bukkit.getScheduler().runTaskLaterAsynchronously(HotFixPlugin.getInstance(), () -> {
                logouts.remove(e.getPlayer().getUniqueId());
            }, 15*20*60L);
        }
    }
}
