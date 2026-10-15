/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.ExpUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;

public class SkillSwipe
extends CustomEnchantment {
    public SkillSwipe() {
        super("Skill Swipe", GeneralUtil.swords, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (target instanceof Player) {
            double random = Math.random();
            double chance = 0.2 * (double)enchantLevel;
            int amountToSteal = enchantLevel * 6;
            int targetXp = ExpUtils.getTotalExperience((Player)target);
            if (targetXp - amountToSteal < 0) {
                amountToSteal = targetXp;
            }
            if (amountToSteal <= 0) {
                return;
            }
            if (random < chance) {
                ExpUtils.setTotalExperience((Player)target, targetXp - amountToSteal);
                ExpUtils.setTotalExperience((Player)user, ExpUtils.getTotalExperience((Player)user) + amountToSteal);
                user.getWorld().spawnEntity(user.getTargetBlock((HashSet<Byte>) null, 1).getLocation(), EntityType.EXPERIENCE_ORB);
            }
        }
    }
}

