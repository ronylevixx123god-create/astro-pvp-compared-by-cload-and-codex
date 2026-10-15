package me.kingsavaqe.cagepvp.listeners;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.immune.ImmuneManager;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Entity implements Listener {
   private final AstroCagePvP instance;

   public Entity(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
      if (!event.isCancelled()) {
         Player damaged;
         Player damager;
         if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            damaged = (Player)event.getEntity();
            damager = (Player)event.getDamager();
         } else {
            label64: {
               if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
                  Projectile projectile = (Projectile)event.getDamager();
                  if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
                     damaged = (Player)event.getEntity();
                     damager = (Player)projectile.getShooter();
                     if (damaged.getUniqueId().equals(damager.getUniqueId())) {
                        return;
                     }
                     break label64;
                  }

                  return;
               }

               return;
            }
         }

         WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
         ImmuneManager immuneManager = this.instance.getImmuneManager();
         FileConfiguration configLoad = this.instance.getConfig();
         if (immuneManager.hasImmune(damager)) {
            event.setCancelled(true);
            damager.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Damage.Immune.Immune.Message")));
            damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
         } else if (immuneManager.hasImmune(damaged)) {
            event.setCancelled(true);
            damager.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Damage.Immune.Player.Message")));
            damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
         } else if (wildPvPManager.hasWildPvP(damager)) {
            WildPvP wildPvP = wildPvPManager.getWildPvP(damager);
            if (wildPvP.isParticipant(damaged)) {
               if (wildPvP.isGrace()) {
                  event.setCancelled(true);
                  damager.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Damage.Grace.Message")));
                  damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
               }

            } else {
               event.setCancelled(true);
               damager.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Damage.Match.Participant.Message")));
               damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            }
         } else if (wildPvPManager.hasWildPvP(damaged)) {
            event.setCancelled(true);
            damager.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Damage.Match.Player.Message")));
            damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
         }
      }
   }
}
