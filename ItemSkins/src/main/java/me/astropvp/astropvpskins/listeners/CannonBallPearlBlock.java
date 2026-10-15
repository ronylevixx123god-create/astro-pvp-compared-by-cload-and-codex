package me.astropvp.astropvpskins.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CannonBallPearlBlock implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPearl(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (CannonballAmulet.affected.contains(e.getPlayer())) {
                e.getPlayer().sendMessage(CC.translate("&c&l(CannonBall Ability)&c Blocked enderpearl!"));
                e.setCancelled(true);
            }
        }
    }
}
