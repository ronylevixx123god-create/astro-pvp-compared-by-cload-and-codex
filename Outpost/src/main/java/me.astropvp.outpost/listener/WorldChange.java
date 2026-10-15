package me.astropvp.outpost.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.outpost.Outpost;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldChange implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void clearEffects(PlayerTeleportEvent e) {
        Bukkit.getScheduler().runTaskLater(Outpost.getInstance(), () -> {
            if (e.getFrom().getWorld() != e.getTo().getWorld() && e.getTo().getWorld().getName().equals("world_outpost_pluto")) {
                e.getPlayer().setMaxHealth(20);
                e.getPlayer().removePotionEffect(PotionEffectType.HEALTH_BOOST);
            }

        }, 10L);
    }
}
