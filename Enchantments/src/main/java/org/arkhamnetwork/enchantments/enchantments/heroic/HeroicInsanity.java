package org.arkhamnetwork.enchantments.enchantments.heroic;

import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class HeroicInsanity extends HeroicCustomEnchantment
{
    public HeroicInsanity() {
        super("Extreme Insanity", "Insanity", GeneralUtil.axe, 8);
        this.max = 8;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        if(!(playerHit instanceof Player)) return;
        Player hitPlayer = (Player) playerHit;
        final boolean enemyHasSword = hitPlayer.getItemInHand().getType().name().contains("_SWORD");

        if (!enemyHasSword) {
            return;
        }


        if (GeneralUtil.isUsingAxe(player)) {
            e.setDamage(e.getDamage() * 1.05);
        }

        if (player instanceof Player) {
            if (SinkOrSwipFlippers.insanityDevourSilenced.contains(player)) {
                return;
            }

            final double mod = 1.0 + (0.04 * level);
            e.setDamage(e.getDamage() * mod);
        }
    }
}
