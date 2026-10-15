package me.astropvp.outpost.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MarsListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPearl(PlayerTeleportEvent e) {
        if (e.getTo().getWorld().getName().equals("world_outpost_mars")) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                e.getPlayer().sendMessage(CC.translate("&c&l(!) &cEnderpearls are disabled in this world!"));
                e.setCancelled(true);
            }
        }
    }
}
