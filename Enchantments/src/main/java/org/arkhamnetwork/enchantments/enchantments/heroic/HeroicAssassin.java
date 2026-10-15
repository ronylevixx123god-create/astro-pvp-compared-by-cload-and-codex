package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class HeroicAssassin extends CustomEnchantment {
    public HeroicAssassin() {
        super("Shadow Assassin", GeneralUtil.swords, 5);
        this.max = 5;
        this.base = 15.0D;
        this.interval = 6.0D;
        Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
            this.heroicEnchantment = true;
            this.nonHeroicVersion = EnchantmentAPI.getEnchantment("Assassin");
        }, 2L);
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (user.getWorld().equals(target.getWorld())) {
            if (!user.getWorld().equals(target.getWorld())) {
                return;
            }
            double distance = user.getLocation().distance(target.getLocation());
            double dmgMod = 1.0;
            double heroicMult = 2.5;
            if (distance < 0.625) {
                dmgMod += 0.015 * (double)enchantLevel;
            }
            if (distance < 1.25) {
                dmgMod += 0.015 * (double)enchantLevel;
            }
            if (distance < 1.875) {
                dmgMod += 0.015 * (double)enchantLevel;
            }
            if (distance < 2.5) {
                dmgMod += 0.015 * (double)enchantLevel;
            }
            if (distance < 3.75) {
                dmgMod += 0.015 * (double)enchantLevel;
            }
            event.setDamage(event.getDamage() * dmgMod);
        }
    }
}
