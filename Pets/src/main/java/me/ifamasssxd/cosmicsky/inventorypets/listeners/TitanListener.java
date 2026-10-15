package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TitanListener implements Listener {
   @EventHandler
   public void onEntityDamage(EntityDamageByEntityEvent event) {
      if (event.getEntity().getType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
         Player damager = (Player)event.getDamager();
         Player entity = (Player)event.getEntity();
         if (damager.hasMetadata("immune_to_teleblock")) {
            AstroDamageHandler.getInstance().getDamageHandler().increaseDamage(12, event, "TitanPet");
         }
      }

   }
}
