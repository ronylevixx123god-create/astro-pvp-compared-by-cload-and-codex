/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poisoned
extends CustomEnchantment {
    public Poisoned() {
        super("Poisoned", GeneralUtil.armor, 2);
        this.max = 4;
        this.base = 14.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (attacker == null) {
            return;
        }
        int time = (int)(Math.random() * (double)level + 0.5 * (double)level);
        int amp = level > 2 ? 1 : 0;
        double random = Math.random();
        double chance = 0.005;
        if (random < chance && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.POISON, time * 20, amp));
        }
    }
}

