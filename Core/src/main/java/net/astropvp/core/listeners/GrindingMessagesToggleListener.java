package net.astropvp.core.listeners;

import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.GrindingMessagesUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class GrindingMessagesToggleListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (GrindingMessagesUtil.isPlayerContained(player))
            AstroCore.grindingMessages.put(player.getUniqueId(), player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (AstroCore.grindingMessages.containsKey(player.getUniqueId()))
            AstroCore.grindingMessages.remove(player.getUniqueId());
    }
}

