/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

final class CreeperArmorListener
implements Listener {
    CreeperArmorListener() {
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && CreeperArmor.creeperArmor.containsKey(((Player)e.getEntity()).getName())) {
            final Player player = (Player)e.getEntity();
            int level = CreeperArmor.creeperArmor.get(player.getName());
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                if (level >= 2) {
                    new BukkitRunnable(){

                        public void run() {
                            player.setVelocity(new Vector().zero());
                        }
                    }.runTaskLater((Plugin)Enchantments.getInstance(), 1L);
                }
                if (level == 3) {
                    GeneralUtil.healPlayer((LivingEntity)player, Math.min(6.0, e.getDamage() * 0.1));
                }
            }
        }
    }
}

