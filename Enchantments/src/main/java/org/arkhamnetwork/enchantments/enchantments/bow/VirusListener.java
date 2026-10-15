/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

class VirusListener
implements Listener {
    VirusListener() {
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Player p;
        if (e.getEntity() instanceof Player && (e.getCause() == EntityDamageEvent.DamageCause.WITHER || e.getCause() == EntityDamageEvent.DamageCause.POISON) && (p = (Player)e.getEntity()).hasMetadata("virusEnchantmentExpire") && System.currentTimeMillis() < ((MetadataValue)p.getMetadata("virusEnchantmentExpire").get(0)).asLong()) {
            int level = ((MetadataValue)p.getMetadata("virusEnchantmentLevel").get(0)).asInt();
            e.setDamage(e.getDamage() * (double)(level + 1));
        }
    }
}

