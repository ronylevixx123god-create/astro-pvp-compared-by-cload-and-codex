/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.ArmorSlot;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Demonforged
extends CustomEnchantment {
    public Demonforged() {
        super("Demonforged", GeneralUtil.swords, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity target, int level, EntityDamageByEntityEvent e) {
        double chance;
        if (!(target instanceof Player)) {
            return;
        }
        double random = Math.random();
        if (random < (chance = 0.04 * (double)level)) {
            this.damageRandomArmor((Player)player);
        }
    }

    private void damageRandomArmor(Player p) {
        ArmorSlot slot = ArmorSlot.getArmorSlotByIndex(ThreadLocalRandom.current().nextInt(4));
        if (slot != null) {
            ArmorUtil.modifyPlayerArmor(p, slot, -1, true);
        }
    }
}

