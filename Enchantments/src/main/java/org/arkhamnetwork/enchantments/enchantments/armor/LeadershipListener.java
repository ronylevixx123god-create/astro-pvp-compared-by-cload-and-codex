package org.arkhamnetwork.enchantments.enchantments.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

final class LeadershipListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Leadership.Leaderships.remove(e.getPlayer().getName());
    }

    @EventHandler()
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getDamager().hasMetadata("leadershipLevel")) {
            final int level = ((Player) e.getDamager()).getMetadata("leadershipLevel").get(0).asInt();
            final boolean koth = e.getDamager().getWorld().getName().equals("world_koth");
            final boolean outpost = e.getDamager().getWorld().getName().startsWith("world_outpost");
            final double incre = Math.min(1.75, 1.0 + level * ((koth || outpost) ? 0.05 : 0.1));
            double damage = e.getDamage();
            e.setDamage(damage * incre);
        }
    }
}
