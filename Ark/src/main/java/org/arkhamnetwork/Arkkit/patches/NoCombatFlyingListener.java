/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

final class NoCombatFlyingListener
        implements Listener {
    NoCombatFlyingListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCombatTag(EntityDamageEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player || e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player || e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile && ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter() instanceof Player) {
            /*if (e.getEntity() instanceof Player) {
                this.disableFlight((Player) e.getEntity());
                e.getEntity().setMetadata("ncf_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            }
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                this.disableFlight((Player) ((EntityDamageByEntityEvent) e).getDamager());
                ((EntityDamageByEntityEvent) e).getDamager().setMetadata("ncf_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            }*/
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile && ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter() instanceof Player) {
                this.disableFlight((Player) ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter());
                ((Player) ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter()).setMetadata("ncf_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            this.disableFlight((Player) e.getEntity());
            e.getEntity().setMetadata("ncf_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        }
    }

    private void disableFlight(Player pl) {
        pl.setFlying(false);
        pl.setAllowFlight(false);
    }
}

