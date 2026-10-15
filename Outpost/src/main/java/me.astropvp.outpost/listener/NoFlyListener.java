package me.astropvp.outpost.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class NoFlyListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }
        if (!player.getWorld().getName().contains("outpost")) {
            return;
        }
        if (event.isFlying()) {
            player.setAllowFlight(false);
            event.setCancelled(true);
        }
    }
}
