/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

class UnfocusListener
implements Listener {
    UnfocusListener() {
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        Player p;
        Projectile pj;
        if (e.getDamager() instanceof Projectile && (pj = (Projectile)e.getDamager()).getShooter() instanceof Player && (p = (Player)pj.getShooter()).hasMetadata("unfocusEnchantmentExpire") && System.currentTimeMillis() < ((MetadataValue)p.getMetadata("unfocusEnchantmentExpire").get(0)).asLong()) {
            e.setDamage(e.getDamage() / 2.0);
            p.sendMessage((Object)ChatColor.DARK_GREEN + "** UNFOCUSED [50% BOW DMG] **");
        }
    }
}

