package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Block
        extends CustomEnchantment {
    public Block() {
        super("Block", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            double chance;
            Player player2 = (Player)player;
            double random = Math.random();
            if (random < (chance = 0.1 * (double)level) && player2.isBlocking()) {
                double reduceDamage = 0.5 * (double)(level / 2);
                //e.setDamage(e.getDamage() * (1.0 - reduceDamage));
                player2.playSound(player2.getLocation(), Sound.ITEM_BREAK, 0.7f, 0.2f);
            }
        }
    }
}
