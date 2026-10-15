/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Ravenous
extends CustomEnchantment {
    public Ravenous() {
        super("Ravenous", GeneralUtil.axe, 7);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        if (!(user instanceof Player)) {
            return;
        }
        Player playerAttacker = (Player)user;
        double random = Math.random();
        if (random < (chance = 0.05 * (double)enchantLevel) && playerAttacker.getFoodLevel() < 20) {
            playerAttacker.setFoodLevel(playerAttacker.getFoodLevel() + 1);
        }
    }
}

