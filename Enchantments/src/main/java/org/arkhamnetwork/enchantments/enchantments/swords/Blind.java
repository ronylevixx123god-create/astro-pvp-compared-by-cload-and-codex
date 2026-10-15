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
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.enchantments.armor.Clarity;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Blind
        extends CustomEnchantment {
    public static String RECENTLY_BLINDED_METADATA = "recentlyBlinded";

    public Blind() {
        super("Blind", GeneralUtil.swords_and_bow, 5);
        this.max = 3;
        this.base = 15.0;
        this.interval = 6.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        int time = enchantLevel * 30;
        double random = Math.random();
        if (random < (chance = 0.1 * (double)enchantLevel)) {
            if (target instanceof Player && !Clarity.canBlindnessApply((Player)target, enchantLevel - 1)) {
                return;
            }
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, enchantLevel - 1));
        }
    }
}
