/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class Stormcaller
extends CustomEnchantment {
    public Stormcaller() {
        super("Stormcaller", GeneralUtil.armor, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double chance;
        double random = Math.random();
        if (random < (chance = 0.005 * (double)level) && attacker != null) {
            if (attacker.equals((Object)player)) {
                return;
            }
            player.getWorld().strikeLightning(attacker.getLocation());
            GeneralUtil.pushAwayEntity(player, (Entity)attacker, 1.5);
        }
    }

    private void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        Vector unitVector = entity.getLocation().toVector().subtract(center.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}

