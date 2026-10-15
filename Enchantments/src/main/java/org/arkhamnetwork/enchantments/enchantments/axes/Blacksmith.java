/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.DurabilityUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class Blacksmith
extends CustomEnchantment {
    public Blacksmith() {
        super("Blacksmith", GeneralUtil.axe, 2);
        this.max = 5;
        this.base = 17.0;
        this.interval = 6.0;
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                Blacksmith.this.heroicVersion = EnchantmentAPI.getEnchantment((String)"Master Blacksmith");
            }
        }, 2L);
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random = Math.random();
        if (random < (chance = 0.075 * (double)level)) {
            DurabilityUtil.healMostDamagedArmorPeice((Player)player, level == 5 ? (short)2 : 1);
            if (level >= 3) {
                DurabilityUtil.healMostDamagedArmorPeice((Player)player, (short)1);
            }
            e.setDamage(e.getDamage() * 0.5D);
        }
    }
}

