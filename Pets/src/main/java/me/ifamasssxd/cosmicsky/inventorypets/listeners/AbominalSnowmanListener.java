package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

public class AbominalSnowmanListener implements Listener {
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onEnderpearl(PlayerTeleportEvent event) {
      if (event.getPlayer().hasMetadata("frozenSnowman") && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
         event.getPlayer().sendMessage(ChatColor.RED + "You cannot enderpearl while you are a frozen snowman!");
         event.setCancelled(true);
      }
   }

   @EventHandler
   public void onEntityDamage(EntityDamageByEntityEvent e) {
      if (e.getDamager().hasMetadata("frozenSnowman")) {
         e.setCancelled(true);
         return;
      }
      if (e.getEntity().getType().equals(EntityType.PLAYER) && e.getDamager().getType().equals(EntityType.PLAYER) && e.getDamager() instanceof Player) {
         Player player = (Player)e.getEntity();
         if (player.hasMetadata("frozenSnowman")) {
            player.removeMetadata("frozenSnowman", CosmicSkyInventoryPets.get());
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.setWalkSpeed(0.2F);
            DisguiseAPI.undisguiseToAll(player);
            player.setMetadata("cancelrunnable", new FixedMetadataValue(CosmicSkyInventoryPets.get(), Long.valueOf(System.currentTimeMillis())));
            player.sendMessage(String.valueOf(CC.Red) + "You are no longer a frozen Snowman! Due to taking damage.");
         }
      }
   }
}
