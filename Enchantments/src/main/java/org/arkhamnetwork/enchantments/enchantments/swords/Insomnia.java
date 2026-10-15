/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Insomnia
        extends CustomEnchantment {
    public Insomnia() {
        super("Insomnia", GeneralUtil.swords, 2);
        this.max = 7;
        this.base = 15.0;
        this.interval = 15.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        int time;
        double random = Math.random();
        double chance = 0.025 * (double)level;
        int n = time = level > 1 ? 6 : 4;
        if (random < chance) {
            //player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 10, Math.min(level - 1, 2)));
            if (level >= 6) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, time * 10, 1));
            }
            double multi = 1.0 + (double)level * 0.05;
            e.setDamage(e.getDamage() * multi);
        }
    }
}
