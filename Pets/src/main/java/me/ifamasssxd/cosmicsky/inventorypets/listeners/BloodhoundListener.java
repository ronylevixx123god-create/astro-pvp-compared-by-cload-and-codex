package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.armorsets.struct.sets.LunaArmorSet;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BloodhoundListener implements Listener {
   @EventHandler
   public void onEntityDamage(EntityDamageByEntityEvent event) {
      if (event.getEntity().getType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
         Player damager = (Player)event.getDamager();
         Player entity = (Player)event.getEntity();
         if (entity.hasMetadata("bloodHoundNoBless")) {
            if (LunaArmorSet.isLunaStriked((Player) event.getEntity())) return;
            if (entity.getMetadata("bloodHoundNoBless").get(0).asString().equals("ancient")) {
               entity.sendMessage(CC.translate("&3&L* LEVIATHAN'S CURSE &7(1.5x DMG) &3&l*"));
            }
            event.setDamage(event.getDamage() * 1.25);
         }
      }

   }
}
