package me.ifamasssxd.cosmicbosses.listeners;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.UndeadAssassin;
import me.ifamasssxd.cosmicbosses.bosses.yijki.Yijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.MiniBoss;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.UUID;

public class WitherBossListener implements Listener {
    Random random;

    public WitherBossListener() {
        this.random = new Random();
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof WitherSkull && e.getEntity().getShooter() != null) {
            final Entity wither = (Entity) e.getEntity().getShooter();
            if (wither.hasMetadata("boss")) {
                final Boss boss = Boss.bosses.get(wither.getUniqueId());
                if (boss == null) {
                    return;
                }
                boss.onMiscUpdate((Event) e);
            }
        }
    }

    @EventHandler
    public void onProjectileLand(final ProjectileHitEvent event) {
        if (event.getEntity().getShooter() == null) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Entity)) {
            return;
        }
        final Entity either = (Entity) event.getEntity().getShooter();
        if (either.hasMetadata("boss")) {
            final Boss boss = Boss.bosses.get(either.getUniqueId());
            if (boss == null) {
                return;
            }
            boss.onMiscUpdate((Event) event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionSplash(final PotionSplashEvent e) {
        final ThrownPotion potion = e.getEntity();
        for (final Entity affected : e.getAffectedEntities()) {
            if (affected.hasMetadata("boss")) {
                final Boss boss = Boss.bosses.get(affected.getUniqueId());
                if (!(boss instanceof GuardBoss) && !(boss instanceof MiniBoss)) {
                    continue;
                }
                e.setIntensity((LivingEntity) affected, 0.0);
            }
        }
        for (final PotionEffect effect : potion.getEffects()) {
            if (effect.getType().equals((Object) PotionEffectType.HARM)) {
                for (final Entity ent : e.getEntity().getNearbyEntities(5.0, 5.0, 5.0)) {
                    if (ent.hasMetadata("boss")) {
                        final Boss boss2 = Boss.bosses.get(ent.getUniqueId());
                        if (boss2 == null || !(boss2 instanceof Yijki) || boss2.getPhase() != Phase.FOUR) {
                            continue;
                        }
                        boss2.getEntity().damage(25.0);
                        if (potion.getShooter() instanceof Player) {
                            final Player thrower = (Player) potion.getShooter();
                            boss2.getDamageTracker().trackDamage((Entity) thrower, 25);
                            break;
                        }
                        break;
                    }
                }
            } else {
                if (!effect.getType().getName().equals(PotionEffectType.INCREASE_DAMAGE.getName())) {
                    continue;
                }
                for (final Entity affected2 : e.getAffectedEntities()) {
                    if (e.getIntensity((LivingEntity) affected2) == 0.0) {
                        continue;
                    }
                    if (affected2 == null || !affected2.hasMetadata("boss")) {
                        continue;
                    }
                    final Boss boss2 = Boss.bosses.get(affected2.getUniqueId());
                    if (boss2 == null || !(boss2 instanceof UndeadAssassin)) {
                        continue;
                    }
                    e.setIntensity((LivingEntity) affected2, 0.0);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity().hasMetadata("boss_block")) {
            event.getEntity().remove();
            event.setCancelled(true);
        } else if (event.getEntity().hasMetadata("chainReactionBlock")) {
            UUID uuid = (UUID)(event.getEntity().getMetadata("chainReactionBlock").get(0)).value();
            Boss boss = (Boss)Boss.bosses.get(uuid);
            if (boss == null) {
                return;
            }
            boss.onMiscUpdate((Event) event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onWitherSkullLand(final ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof WitherSkull) {
            final WitherSkull skull = (WitherSkull) event.getEntity();
            if (skull.getShooter() instanceof Entity) {
                final Entity wither = (Entity) skull.getShooter();
                if (wither.hasMetadata("boss") && BossUtils.isWarzone(skull.getLocation()) && !WorldGuardUtils.getRegionName(skull.getLocation()).startsWith("spawn_") && this.random.nextInt(10) <= 1) {
                    int bossId = -1;
                    final Boss boss = Boss.bosses.get(wither.getUniqueId());
                    if (boss != null) {
                        bossId = boss.getEntity().getEntityId();
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
