/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

public class Piercing
extends CustomEnchantment {
    public Piercing() {
        super("Piercing", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double armor_percent_ignored = (double)level * 2.5;
        if (e.isCancelled() || !(playerHit instanceof Player)) {
            return;
        }
        if (playerHit.hasMetadata("lastArrowDamageEvent")) {
            long minDelayMilliseconds = level * 200;
            long last = ((MetadataValue)playerHit.getMetadata("lastArrowDamageEvent").get(0)).asLong();
            if (System.currentTimeMillis() - last <= minDelayMilliseconds) {
                return;
            }
        }
        double extra_dmg = armor_percent_ignored / 100.0 * e.getDamage();
        playerHit.damage(extra_dmg);
        ((Player)player).playSound(player.getLocation(), Sound.ARROW_HIT, 1.2f, 0.6f);
        try {
            de.inventivegames.particle.ParticleEffect.CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getLocation(), (float) Math.random(), (float)Math.random(), (float)Math.random(), 0.5f, 20);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

