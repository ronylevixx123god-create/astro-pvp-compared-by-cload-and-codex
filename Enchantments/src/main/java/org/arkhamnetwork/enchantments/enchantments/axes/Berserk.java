/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Berserk
extends CustomEnchantment {
    public Berserk() {
        super("Berserk", GeneralUtil.axe, 7);
        this.max = 5;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!(playerHit instanceof Player)) {
            return;
        }
        Player playerAttacker = (Player)player;
        double random = Math.random();
        double chance = 0.03 * (double)level;
        double duration = level * 2;
        if (random < chance) {
            playerAttacker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int)duration * 20, 1));
        }
    }
}

