/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Greatsword
extends CustomEnchantment {
    public Greatsword() {
        super("Greatsword", GeneralUtil.swords, 7);
        this.max = 5;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        boolean enemyHasBow = playerHit.getEquipment().getItemInHand().getType().name().equals("BOW");
        if (!enemyHasBow) {
            return;
        }
        double mod = 1.0 + 0.05 * (double)level;
        e.setDamage(e.getDamage() * mod);
    }
}

