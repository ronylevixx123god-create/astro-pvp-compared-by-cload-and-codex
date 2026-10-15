package me.astropvp.astrospacegrinding.listeners;

import com.compilex.CosmicOutposts.CosmicOutpostsAPI;
import me.ifamasssxd.cosmichcf.CosmicHCFOutposts;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.managers.OutpostManager;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StrongholdBuffsListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getLocation().getWorld().getName().contains("space") && HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE)) {
                event.setDamage(event.getDamage() * 1.15);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getLocation().getWorld().getName().contains("space") && HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE)) {
                event.setDamage(event.getDamage() * 0.85);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageZombie(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getLocation().getWorld().getName().contains("space") && HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE)) {
                event.setDamage(event.getDamage() * 1.25);
            }
        }
    }
}
