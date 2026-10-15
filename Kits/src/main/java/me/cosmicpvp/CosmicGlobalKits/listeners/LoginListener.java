/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package me.cosmicpvp.CosmicGlobalKits.listeners;

import java.util.Map;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import me.cosmicpvp.CosmicGlobalKits.utils.GlobalCooldowns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        GlobalKitUser user = new GlobalKitUser(player.getUniqueId());
        CosmicGlobalKits.getPlugin().getGlobalKitUsers().put(player.getUniqueId(), user);
        user.loadGlobalKitData(true, gkitUser -> {
            if (!gkitUser.getGlobalKits().isEmpty()) {
                GlobalCooldowns.clearCooldowns(e.getPlayer());
                for (Map.Entry<GlobalKits, GlobalKitData> data : gkitUser.getGlobalKits().entrySet()) {
                    GlobalCooldowns.setCooldown(e.getPlayer(), data.getKey(), data.getValue().getLastUsed());
                }
            }
        });
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        CosmicGlobalKits.getPlugin().getGlobalKitUsers().remove(e.getPlayer().getUniqueId());
    }
}

