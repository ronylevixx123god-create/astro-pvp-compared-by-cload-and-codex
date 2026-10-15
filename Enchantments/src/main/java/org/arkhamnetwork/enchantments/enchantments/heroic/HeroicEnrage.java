package org.arkhamnetwork.enchantments.enchantments.heroic;

import lombok.SneakyThrows;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public class HeroicEnrage extends HeroicCustomEnchantment
{
    public HeroicEnrage() {
        super("Unrestrained Enrage", "Enrage", GeneralUtil.swords, 5);
        this.max = 3;
        this.base = 15.0;
        this.interval = 6.0;
    }

    @SneakyThrows
    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        /*
        double health = (double) user.getClass().getMethod("getHealth").invoke(user);
        double maxHealth = (double) user.getClass().getMethod("getMaxHealth").invoke(user);
        double hpPercent = health / maxHealth;
        double multi = 1.0D;
        if (hpPercent <= 0.75D) {
            multi += 0.1D;
        }

        if (hpPercent <= 0.5D) {
            multi += 0.1D;
        }

        if (hpPercent <= 0.25D) {
            multi += 0.1D;
        }


         */
        event.setDamage(event.getDamage() * 1.05);
    }
}
