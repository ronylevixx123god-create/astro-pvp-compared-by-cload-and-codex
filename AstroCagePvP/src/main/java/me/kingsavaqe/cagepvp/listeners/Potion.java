package me.kingsavaqe.cagepvp.listeners;

import java.util.Iterator;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.immune.ImmuneManager;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

public class Potion implements Listener {
   private final AstroCagePvP instance;

   public Potion(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPotionSplash(PotionSplashEvent event) {
      if (event.getAffectedEntities().size() != 0) {
         ThrownPotion thrownPotion = event.getPotion();
         WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
         ImmuneManager immuneManager = this.instance.getImmuneManager();
         Iterator<LivingEntity> affectedEntitiesIterator = event.getAffectedEntities().iterator();
         if (thrownPotion.getShooter() != null && thrownPotion.getShooter() instanceof Player) {
            Player shooter = (Player)thrownPotion.getShooter();
            if (wildPvPManager.hasWildPvP(shooter)) {
               WildPvP wildPvP = wildPvPManager.getWildPvP(shooter);

               while(affectedEntitiesIterator.hasNext()) {
                  LivingEntity livingEntity = (LivingEntity)affectedEntitiesIterator.next();
                  if (livingEntity instanceof Player) {
                     Player player = (Player)livingEntity;
                     if (!player.getUniqueId().equals(shooter.getUniqueId()) && !wildPvP.isParticipant(player)) {
                        affectedEntitiesIterator.remove();
                        event.setIntensity(livingEntity, 0.0D);
                     }
                  }
               }
            } else {
               LivingEntity livingEntity;
               Player player;
               if (immuneManager.hasImmune(shooter)) {
                  while(affectedEntitiesIterator.hasNext()) {
                     livingEntity = (LivingEntity)affectedEntitiesIterator.next();
                     if (livingEntity instanceof Player) {
                        player = (Player)livingEntity;
                        if (!player.getUniqueId().equals(shooter.getUniqueId())) {
                           affectedEntitiesIterator.remove();
                           event.setIntensity(livingEntity, 0.0D);
                        }
                     }
                  }
               } else {
                  while(true) {
                     do {
                        do {
                           do {
                              if (!affectedEntitiesIterator.hasNext()) {
                                 return;
                              }

                              livingEntity = (LivingEntity)affectedEntitiesIterator.next();
                           } while(!(livingEntity instanceof Player));

                           player = (Player)livingEntity;
                        } while(player.getUniqueId().equals(shooter.getUniqueId()));
                     } while(!wildPvPManager.hasWildPvP(player) && !immuneManager.hasImmune(player));

                     affectedEntitiesIterator.remove();
                     event.setIntensity(livingEntity, 0.0D);
                  }
               }
            }
         } else {
            while(true) {
               LivingEntity livingEntity;
               Player player;
               do {
                  do {
                     if (!affectedEntitiesIterator.hasNext()) {
                        return;
                     }

                     livingEntity = (LivingEntity)affectedEntitiesIterator.next();
                  } while(!(livingEntity instanceof Player));

                  player = (Player)livingEntity;
               } while(!wildPvPManager.hasWildPvP(player) && !immuneManager.hasImmune(player));

               affectedEntitiesIterator.remove();
               event.setIntensity(livingEntity, 0.0D);
            }
         }

      }
   }
}
