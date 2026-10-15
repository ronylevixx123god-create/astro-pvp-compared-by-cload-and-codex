package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import java.util.*;
import org.arkhamnetwork.enchantments.enchantments.axes.*;
import org.bukkit.*;

public class Inversion extends CustomEnchantment
{
    public Inversion() {
        super("Inversion", GeneralUtil.swords, 2);
        this.max = 4;
        this.base = 17.0;
        this.interval = 4.0;
    }
    public void applyDefenseEffect(LivingEntity entityHit, LivingEntity attacker, int level, EntityDamageEvent e) {
        float procChance = (float)(0.05D * (double)level);
        if (Math.random() <= (double)procChance) {
            Player playerHit = (Player)entityHit;
            double heal = (double)((new Random()).nextInt(3) + 1);
            if (heal >= 1.0D) {
                if (Corrupt.isCorrupted(playerHit) && Math.random() < (double)((float)Corrupt.getCorruptLevel(playerHit) * 0.2F)) {
                    playerHit.damage(heal);
                    playerHit.sendMessage(ChatColor.DARK_PURPLE + "* CORRUPTED [" + ChatColor.RED + heal + ChatColor.DARK_PURPLE + " DMG] *");
                    return;
                }

                GeneralUtil.healPlayer(playerHit, heal);
                e.setDamage(0.0);
                //e.setCancelled(true);
                playerHit.playSound(playerHit.getLocation(), Sound.PISTON_EXTEND, 0.8F, 2.0F);
            }
        }

    }
}
