package org.arkhamnetwork.enchantments.enchantments.armor;

import org.bukkit.event.*;
import org.bukkit.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.block.*;
import org.arkhamnetwork.enchantments.util.*;

final class acenstoralListener implements Listener
{
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager().hasMetadata("acenstoralSummoner")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("acenstoralSummoner")) {
            Spirits.spirits.remove(e.getEntity());
            Spirits.ticks.remove(e.getEntity().getEntityId());
            e.getDrops().clear();
            e.getEntity().remove();
        }
        if (e.getEntity().hasMetadata("targetTaskID")) {
            Bukkit.getScheduler().cancelTask(e.getEntity().getMetadata("targetTaskID").get(0).asInt());
        }
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getEntity().hasMetadata("acenstoralSummoner")) {
            e.setCancelled(true);
            e.setTarget(null);
        }
    }

    @EventHandler
    public void onProjectileLaunchEvent(final ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Fireball) {
            final Entity shooter = (Entity)e.getEntity().getShooter();
            if (shooter != null && shooter.getType() == EntityType.BLAZE && shooter.hasMetadata("acenstoralSummoner")) {
                e.setCancelled(true);
                ((Blaze)shooter).setTarget(null);
                final Projectile orig_proj = e.getEntity();
                orig_proj.setBounce(false);
                orig_proj.setMetadata("spiritFireball", new FixedMetadataValue((Plugin)Enchantments.getInstance(), true));
                Enchantments.getInstance().getServer().getScheduler().scheduleSyncDelayedTask((Plugin)Enchantments.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (!orig_proj.hasMetadata("no_spawn")) {
                            final Projectile FB = ((Blaze)shooter).launchProjectile((Class<? extends Projectile>)SmallFireball.class);
                            FB.setMetadata("no_spawn", new FixedMetadataValue((Plugin)Enchantments.getInstance(), true));
                            FB.setVelocity(orig_proj.getVelocity());
                            FB.setBounce(false);
                            FB.setMetadata("spiritFireball", new FixedMetadataValue((Plugin)Enchantments.getInstance(), true));
                        }
                    }
                }, 10L);
            }
        }
    }

    @EventHandler
    public void onExplosionPrime(final ExplosionPrimeEvent e) {
        if (e.getEntity().hasMetadata("spiritFireball") || e.getEntity().getWorld().getName().equals("world_duels") || e.getEntity().getWorld().getName().equals("world_duels2")) {
            e.setRadius(0.0f);
            e.setFire(false);
        }
    }

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent e) {
        if ((e.getIgnitingEntity() != null && e.getIgnitingEntity().hasMetadata("spiritFireball")) || e.getBlock().getWorld().getName().equals("world_duels") || e.getBlock().getWorld().getName().equals("world_duels2")) {
            e.setCancelled(true);
        }
    }

    private boolean isAlly(final Player p1, final Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}
