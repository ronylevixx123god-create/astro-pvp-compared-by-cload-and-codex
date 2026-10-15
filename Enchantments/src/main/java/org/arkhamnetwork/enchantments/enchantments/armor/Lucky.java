package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

import java.util.Random;

public class Lucky extends CustomEnchantment
{
    public Lucky() {
        super("Lucky", GeneralUtil.armor, 3);
        this.max = 10;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (player instanceof Player && new Random().nextInt(400) <= level) {
            Player p = (Player)player;
            double dmg = e.getDamage();
            dmg *= (double)ArmorUtil.getArmorDamageNullificationPercent(ArmorUtil.getArmorValue(p));
            if (player.getHealth() - dmg <= 0.0) {
                //e.setCancelled(true);
                e.setDamage(0.0);
            }
        }
    }
}
