package com.compilex.CosmicKOTH.listeners;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KothPortalListener implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPortalEnter(EntityPortalEnterEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getWorld().getName().equals("world") && WorldGuardUtils.getRegionsIn(player.getLocation()).contains("desertkoth2")) {
/*                if (CombatLog.inCombat(player)) {
                    player.sendMessage(CC.translate("&c&l(!)&c You cannot enter the koth exit portal while in combat"));
                    return;
                }*/
                Location location = new Location(Bukkit.getWorld("world"), 2422, 120, 4638);
                Bukkit.getScheduler().runTaskLater(CosmicKOTHPlugin.get_(), () -> player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN), 3L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPortalEnter(PlayerTeleportEvent event) {
        if(event.getTo() == null) {
            return;
        }
        Player player = event.getPlayer();
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);

/*            if (CombatLog.inCombat(player)) {
                player.sendMessage(CC.translate("&c&l(!)&c You cannot enter the koth exit portal while in combat"));
                return;
            }*/

            Location location = new Location(Bukkit.getWorld("world"), 2422, 120, 4638);

            Bukkit.getScheduler().runTaskLater(CosmicKOTHPlugin.get_(), () -> player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN), 3L);
        }
    }
}
