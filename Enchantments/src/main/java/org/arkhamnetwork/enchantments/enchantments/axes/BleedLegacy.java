/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BleedLegacy
extends CustomEnchantment {
    public BleedLegacy() {
        super("Bleed", GeneralUtil.axe, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 3.0;
        Bukkit.getPluginManager().registerEvents((Listener)new BleedListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!(playerHit instanceof Player)) {
            return;
        }
        double random = Math.random();
        double chance = 0.075 * (double)level;
        double chance1 = 0.025 * (double)level;
        int time = 2 * level;
        double amp = level / 2;
        if (amp < 0.5) {
            amp = 0.5;
        }
        if (random < chance && playerHit instanceof Player) {
            if (random < chance1) {
                //playerHit.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, level - 1));
            }
            if (!playerHit.hasMetadata("ce_bleeding")) {
                new BleedEvent((Player)playerHit, amp, time).start();
            }
        }
    }
}

