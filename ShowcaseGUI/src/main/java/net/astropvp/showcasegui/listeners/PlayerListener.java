package net.astropvp.showcasegui.listeners;

import net.astropvp.showcasegui.ShowcaseAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onPlayerJoin(PlayerJoinEvent e) {
        ShowcaseAPI.loadShowcase(e.getPlayer().getUniqueId(), e.getPlayer().getName(), true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        ShowcaseAPI.showcases.remove(e.getPlayer().getUniqueId());
    }


}
