package org.arkhamnetwork.enchantments.enchantments.swords;

import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.*;
import org.arkhamnetwork.enchantments.*;
import com.rit.sucy.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public class Assassin extends CustomEnchantment {
    public Assassin() {
        super("Assassin", GeneralUtil.swords, 5);
        this.max = 5;
        this.base = 15.0;
        this.interval = 6.0;
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
            this.heroicVersion = EnchantmentAPI.getEnchantment((String)"Shadow Assassin");
        }, 2L);
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (!user.getWorld().equals(target.getWorld())) {
            return;
        }
        double distance = user.getLocation().distance(target.getLocation());
        double dmgMod = 1.0;
        if (distance < 0.25) {
            dmgMod += 0.01 * (double)enchantLevel;
        }
        if (distance < 0.5) {
            dmgMod += 0.01 * (double)enchantLevel;
        }
        if (distance < 0.75) {
            dmgMod += 0.01 * (double)enchantLevel;
        }
        if (distance < 1.0) {
            dmgMod += 0.01 * (double)enchantLevel;
        }
        if (distance < 1.5) {
            dmgMod += 0.01 * (double)enchantLevel;
        }
        if (distance > 2.0) {
            dmgMod -= 0.01 * (double)enchantLevel;
        }
        if (distance > 2.5) {
            dmgMod -= 0.0125 * (double)enchantLevel;
        }
        if (distance > 3.0) {
            dmgMod -= 0.025 * (double)enchantLevel;
        }
        event.setDamage(event.getDamage() * dmgMod);
    }
}
