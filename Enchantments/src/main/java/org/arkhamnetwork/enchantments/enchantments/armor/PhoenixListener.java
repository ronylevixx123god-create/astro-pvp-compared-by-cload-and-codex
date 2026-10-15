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
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

final class PhoenixListener
        implements Listener {
    Phoenix enchantment;

    protected PhoenixListener(Phoenix enchantment) {
        this.enchantment = enchantment;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamage(EntityDamageEvent e) {
        Player p;
        if (e.getEntity() instanceof Player && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && Phoenix.hasPhoenixEquipped(p = (Player)e.getEntity())) {
            this.enchantment.applyDefenseEffect((LivingEntity)p, null, Phoenix.phoenixEquipped.get(p.getUniqueId()), e);
        }
    }

    @EventHandler
    public void onExtraDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (!Phoenix.canUsePhoenix((Player) e.getEntity())) {
                if (AstroItemSkinsAPI.hasSkinEquipped(((Player) e.getDamager()).getPlayer(), SkinType.VALENTINES_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped(((Player) e.getDamager()).getPlayer(), SkinType.VALENTINES_2022_MUTATED_SWORD)) {
                    e.setDamage(e.getDamage() * 1.1);
                }
            }
        }
    }
}
