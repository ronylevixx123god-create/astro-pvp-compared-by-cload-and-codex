package me.ifamasssxd.cosmicbosses.bosses;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer.Enforcer;
import me.ifamasssxd.cosmicbosses.evilabomination.EvilAbomination;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public interface GuardBoss {
    default void checkGuardLocation(final Boss boss, final Location chest) {
        final LivingEntity ent = boss.getEntity();
        if (ent.getLocation().getWorld() != chest.getWorld()) {
            this.teleportToChest(boss, chest);
            return;
        }
        final Creature creature = (Creature) ent;
        if (creature.getTarget() != null && creature.getTarget().getWorld() != chest.getWorld()) {
            creature.setTarget((LivingEntity) null);
        }
        if (creature.getTarget() == null) {
            final double distance = creature.getLocation().distanceSquared(chest);
            if (distance > this.getDistance()) {
                this.teleportToChest(boss, chest);
            }
        }
    }

    default void onDamage(final Boss boss, final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
                    event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 1);


            return;
        }
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            return;
        }
        event.setCancelled(true);
    }

    default int checkOutsideTicks(final Boss boss, final int currentTicks, final Location chest) {
        final Creature creature = (Creature) boss.getEntity();
        if (creature.getTarget() == null || (creature.getTarget().getWorld() == chest.getWorld() && (creature.getTarget().getLocation().distanceSquared(chest) <= 200.0 || creature.getLocation().distanceSquared(chest) <= 150.0))) {
            return 0;
        }
        final int newTicks = currentTicks + 1;
        if (newTicks >= 5) {
            creature.setTarget((LivingEntity) null);
            this.teleportToChest(boss, chest);
            return 0;
        }
        return newTicks;
    }

    default int getDistance() {
        return (this instanceof Enforcer) ? 70 : 35;
    }

    default void teleportToChest(final Boss boss, final Location chestLocation) {
        boss.getEntity().teleport(chestLocation.clone().add(0.0, 1.0, 0.0));
        if (boss instanceof EvilAbomination) {
            //ParticleEffect.sendToLocation(ParticleEffect.CLOUD, boss.getEntity().getLocation(), 0.5f, 0.5f, 0.5f, 0.01f, 5);
            try {
                de.inventivegames.particle.ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), boss.getEntity().getLocation(), 0.5f, 0.5f, 0.5f, 0.01f, 6);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
