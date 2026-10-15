/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Marksman
extends CustomEnchantment {
    public Marksman() {
        super("Marksman", GeneralUtil.armor, 4);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            int marksmanLevel = 0;
            for (ItemStack i : player.getEquipment().getArmorContents()) {
                Map enchants;
                if (i == null || !(enchants = EnchantmentAPI.getEnchantments((ItemStack)i)).containsKey((Object)this)) continue;
                marksmanLevel += ((Integer)enchants.get((Object)this)).intValue();
            }
            if (marksmanLevel > 0) {
                double mod = 1.0 + (double)marksmanLevel * 0.015625;
                e.setDamage(e.getDamage() * mod);
            }
        }
    }
}

