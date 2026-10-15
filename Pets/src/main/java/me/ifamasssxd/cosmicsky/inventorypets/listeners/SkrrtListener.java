package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SkrrtListener implements Listener {
   @EventHandler(priority = EventPriority.LOWEST)
   public void onEntityDamage(EntityDamageByEntityEvent event) {
      Entity entity = event.getEntity();
      Entity damager = event.getDamager();

      if (entity instanceof Player && damager instanceof Player) {

         if (entity.hasMetadata("frozenSkrrt")) {
            event.setCancelled(true);
         }

         if (damager.hasMetadata("frozenSkrrt")) {
            event.setCancelled(true);
         }

      }
   }
}
