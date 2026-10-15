package net.astropvp.core.listeners;

import net.astropvp.core.cachers.LastManStandingCacher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LastManStandingListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LastManStandingCacher cacher = LastManStandingCacher.getPlayer(player);
        if (cacher != null)
            cacher.remove();
    }
}