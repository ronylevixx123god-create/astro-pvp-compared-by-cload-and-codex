/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class Farcast
extends CustomEnchantment {
    public Farcast() {
        super("Farcast", new Material[]{Material.BOW}, 3);
        this.max = 5;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(LivingEntity entityHit, LivingEntity attacker, int level, EntityDamageEvent e) {
        float procChance = 0.1f + (float)(0.025 * (double)level / Math.max(0.25, entityHit.getHealth() / entityHit.getMaxHealth()));
        if (Math.random() <= (double)procChance && attacker != null && !GeneralUtil.canEffectEntity((Entity)entityHit)) {
            try {
                ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), attacker.getLocation().add(0.0, 1.0, 0.0), 1.0f, 1.0f, 1.0f, 0.1f, 3);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            GeneralUtil.pushAwayEntity(entityHit, (Entity)attacker, 2.3);
        }
    }

    private void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        Vector unitVector = entity.getLocation().toVector().subtract(center.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}

