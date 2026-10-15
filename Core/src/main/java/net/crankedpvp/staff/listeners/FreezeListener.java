package net.crankedpvp.staff.listeners;

import net.astropvp.core.AstroCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class FreezeListener implements Listener {

    private AstroCore plugin;
    public FreezeListener(AstroCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event) {
        if(event.getEntity() == null) return;
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getDamager() == null) return;
        if(!(event.getDamager() instanceof Player)) return;

        Player victim = ((Player) event.getEntity());
        Player attacker = ((Player) event.getDamager());

        if(this.plugin.getFreezeManager().getFrozenPlayers().containsKey(victim.getUniqueId()) || this.plugin.getFreezeManager().getFrozenPlayers().containsKey(attacker.getUniqueId())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        if(event.getPlayer() == null) return;
        if(this.plugin.getFreezeManager().getFrozenPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void pickup(PlayerPickupItemEvent event) {
        if(event.getPlayer() == null) return;
        if(this.plugin.getFreezeManager().getFrozenPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            return;
        }
    }
}
