package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Treasure extends CustomEnchantment {
    public Treasure() {
        super("Treasure", GeneralUtil.swords, 1);
        this.max = 5;
        this.base = 25.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        //coded in spawners
    }

}

