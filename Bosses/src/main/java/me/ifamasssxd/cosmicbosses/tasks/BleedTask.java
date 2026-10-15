package me.ifamasssxd.cosmicbosses.tasks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BleedTask implements Runnable {
    public void run() {
        for (Map.Entry<LivingEntity, Long> entry : bleedingEntities.entrySet()) {
            if (entry.getKey() == null || ((LivingEntity)entry.getKey()).isDead()) {
                bleedingEntities.remove(entry.getKey());
                continue;
            }
            LivingEntity ent = entry.getKey();
            if (WorldGuardUtils.isPvPDisabled(ent.getLocation())) {
                bleedingEntities.remove(ent);
                continue;
            }
            long time = ((Long)entry.getValue()).longValue();
            if (time > System.currentTimeMillis()) {
                int damage = 2;
                if (ent.getHealth() <= 0.0D)
                    continue;
                ent.damage(0.0D);
                if (ent.getHealth() - 2.0D <= 0.0D) {
                    ent.setHealth(0.0D);
                    continue;
                }
                ent.setHealth(ent.getHealth() - 2.0D);
                continue;
            }
            bleedingEntities.remove(ent);
        }
    }

    public static void bleedPlayer(Player p, int seconds) {
        bleedingEntities.put(p, Long.valueOf(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds)));
    }

    public static boolean isBleeding(Player p) {
        return bleedingEntities.containsKey(p);
    }

    public static void stopBleed(Player p) {
        bleedingEntities.remove(p);
    }

    private static ConcurrentHashMap<LivingEntity, Long> bleedingEntities = new ConcurrentHashMap<>();
}
