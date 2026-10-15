/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Lightning
extends CustomEnchantment {
    public Lightning() {
        super("Lightning", new Material[]{Material.BOW}, 2);
        this.max = 3;
        this.base = 20.0;
        this.interval = 0.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random = Math.random() * 1.0;
        if (random < (chance = 0.02 * (double)level)) {
            if (!(playerHit instanceof Player)) {
                return;
            }
            playerHit.getLocation().getWorld().strikeLightningEffect(playerHit.getLocation());
            playerHit.damage(2.0, (Entity)player);
        }
    }
}

