/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValue;

final class InfernalListener
implements Listener {
    private boolean factions_enabled = Bukkit.getPluginManager().isPluginEnabled("Factions");

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata(Infernal.infernalEncahntmentMetadata)) {
            int level;
            Location loc = e.getEntity().getLocation();
            if (WorldGuardUtils.isPvPDisabled(loc)) {
                return;
            }
            int radius = level = ((MetadataValue)e.getEntity().getMetadata(Infernal.infernalEncahntmentMetadata).get(0)).asInt();
            String enchantment_owner = e.getEntity().hasMetadata(Infernal.infernalEncahntmentOwnerMetadata) ? ((MetadataValue)e.getEntity().getMetadata(Infernal.infernalEncahntmentOwnerMetadata).get(0)).asString() : null;
            for (Entity ent : e.getEntity().getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                if (ent.hasMetadata("spectator") || ent.hasMetadata("do_not_clear") || !(ent instanceof LivingEntity) || enchantment_owner != null && ent instanceof Player && (((Player)ent).getName().equals(enchantment_owner) || this.isAlly((Player)ent, Bukkit.getPlayer((String)enchantment_owner)))) continue;
                ent.setFireTicks(radius * 20);
            }
            try {
                ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.5f, 20);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}

