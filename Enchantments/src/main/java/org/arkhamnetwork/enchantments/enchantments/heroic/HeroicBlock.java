package org.arkhamnetwork.enchantments.enchantments.heroic;

import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class HeroicBlock extends HeroicCustomEnchantment
{
    public HeroicBlock() {
        super("Reflective Block", "Block", false);
        this.setNaturalMaterials(GeneralUtil.swords);
        this.max = 3;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(final LivingEntity damaged, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (damaged instanceof Player) {
            double chance;
            if (damaged.hasMetadata("neutralized")) {
                return;
            }
            Player player = (Player)damaged;
            double random = Math.random();
            if (random < (chance = (player.isBlocking() ? 0.16 : 0.7) * (double)level)) {
                double reduceDamage = 0.1 * (double)level;
                double finalDamage = Math.max(0.0, e.getFinalDamage());
                if (finalDamage <= 0.0) {
                    return;
                }
                double damageToReflect = reduceDamage;
                e.setDamage(e.getDamage() * reduceDamage);
                double reflectChance = 0.08 * (double)level + (player.isBlocking() ? 0.05 : 0.0);
                if (Math.random() < reflectChance && attacker instanceof Player) {
                    player.sendMessage(CC.GreenB + "* BLOCKED §7[" + NumberUtils.formatSeconds((double)finalDamage) + "]§&§l *");
                    if (damageToReflect > 0.0) {
                        Player attacking = (Player)attacker;
                        attacking.damage(damageToReflect);
                        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_METAL, 10.0f, 2.0f);
                        attacking.sendMessage(CC.RedB + "* " + player.getName() + " REFLECTIVE BLOCK [" + CC.Gray + (int)(reduceDamage * 100.0) + "% DMG" + CC.RedB + "]");
                    }
                }
            }
        }
    }
}
