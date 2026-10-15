/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;

final class UndeadRuseListener
implements Listener {
    UndeadRuseListener() {
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Player pSummoner;
        String summoner;
        Player p;
        if (e.getEntity() instanceof Player && e.getDamager().hasMetadata("undeadruseSummoner")) {
            p = (Player)e.getEntity();
            summoner = ((MetadataValue)e.getDamager().getMetadata("undeadruseSummoner").get(0)).asString();
            pSummoner = Bukkit.getPlayer((String)summoner);
            if (pSummoner != null && (summoner.equalsIgnoreCase(p.getName()) || GeneralUtil.isAlly(p, pSummoner))) {
                //e.setDamage(0.0);
            }
        }
        if (e.getEntity().hasMetadata("undeadruseSummoner") && e.getDamager() instanceof Player) {
            p = (Player)e.getDamager();
            summoner = ((MetadataValue)e.getEntity().getMetadata("undeadruseSummoner").get(0)).asString();
            pSummoner = Bukkit.getPlayer((String)summoner);
            if (pSummoner != null && (summoner.equalsIgnoreCase(p.getName()) || GeneralUtil.isAlly(p, pSummoner))) {
                //e.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getEntity().hasMetadata("undeadruseSummoner")) {
            Player p = (Player)e.getTarget();
            String summoner = ((MetadataValue)e.getEntity().getMetadata("undeadruseSummoner").get(0)).asString();
            Player pSummoner = Bukkit.getPlayer((String)summoner);
            if (pSummoner != null && (summoner.equalsIgnoreCase(p.getName()) || GeneralUtil.isAlly(p, pSummoner) || p.hasMetadata("spectator"))) {
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("undeadruseSummoner")) {
            e.getDrops().clear();
            e.getEntity().remove();
        }
    }
}

