/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerCommandEvent
 */
package me.cosmicpvp.CosmicGlobalKits.listeners;

import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class PermissionListener
implements Listener {
    @EventHandler
    public void onConsoleGiveGkit(ServerCommandEvent event) {
        if (event.getCommand() != null && event.getCommand().startsWith("pex user") && event.getCommand().contains("cosmickits.kit")) {
            String split = event.getCommand().split("pex user ")[1];
            String permNode = split.split("add")[1].trim();
            if (permNode.contains(" ")) {
                permNode = permNode.split(" ")[0].trim();
            }
            if (!permNode.contains("cosmickits")) {
                return;
            }
            try {
                GlobalKits kit = GlobalKits.valueOf(permNode.replace("cosmickits.kit.", ""));
                String playerName = split.split("add")[0].trim();
                Player player = Bukkit.getPlayer((String)playerName);
                if (player != null) {
                    CosmicGlobalKitsAPI.registerGkitLevels(player, kit);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

