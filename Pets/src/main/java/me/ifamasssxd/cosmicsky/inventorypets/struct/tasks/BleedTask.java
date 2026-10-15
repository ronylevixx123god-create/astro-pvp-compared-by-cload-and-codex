package me.ifamasssxd.cosmicsky.inventorypets.struct.tasks;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BleedTask implements Runnable {
   private static ConcurrentHashMap bleedingEntities = new ConcurrentHashMap();

   public void run() {
      Iterator var1 = bleedingEntities.entrySet().iterator();

      while(true) {
         while(var1.hasNext()) {
            Entry entry = (Entry)var1.next();
            if (entry.getKey() != null && !((LivingEntity)entry.getKey()).isDead()) {
               LivingEntity ent = (LivingEntity)entry.getKey();
               if (WorldGuardUtils.isPvPDisabled(ent.getLocation())) {
                  bleedingEntities.remove(ent);
               } else {
                  long time = (Long)entry.getValue();
                  if (time > System.currentTimeMillis()) {
                     boolean damage = true;
                     if (ent.getHealth() > 0.0D) {
                        ent.damage(0.0D);
                        if (ent.getHealth() - 1.0D <= 0.0D) {
                           ent.setHealth(0.0D);
                        } else {
                           ent.setHealth(ent.getHealth() - 1.5D);
                        }
                     }
                  } else {
                     bleedingEntities.remove(ent);
                  }
               }
            } else {
               bleedingEntities.remove(entry.getKey());
            }
         }

         return;
      }
   }

   public static void bleedPlayer(Player p, int seconds) {
      bleedingEntities.put(p, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((long)seconds));
   }

   public static boolean isBleeding(Player p) {
      return bleedingEntities.containsKey(p);
   }

   public static void stopBleed(Player p) {
      bleedingEntities.remove(p);
   }
}
