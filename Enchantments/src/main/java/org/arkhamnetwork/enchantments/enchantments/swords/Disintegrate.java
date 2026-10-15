/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.ArmorSlot;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Disintegrate
extends CustomEnchantment {
    public Disintegrate() {
        super("Disintegrate", GeneralUtil.swords, 5);
        this.max = 4;
        this.base = 15.0;
        this.interval = 6.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (Math.random() <= 0.02 * (double)enchantLevel && target instanceof Player) {
            ((Player)user).playSound(target.getLocation(), Sound.ANVIL_BREAK, 0.3f, 0.8f);
            Player pTarget = (Player)target;
/*            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.HELMET, -1, false);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.CHESTPLATE, -2, false);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.LEGGINGS, -2, false);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.BOOTS, -1, false);*/
        }
    }
}

