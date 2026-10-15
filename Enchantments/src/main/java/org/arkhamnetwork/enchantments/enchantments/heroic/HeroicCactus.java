package org.arkhamnetwork.enchantments.enchantments.heroic;

import org.arkhamnetwork.enchantments.enchantments.swords.Rage;
import org.arkhamnetwork.enchantments.util.CacheUtils;
import org.arkhamnetwork.enchantments.util.RageStacksCache;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class HeroicCactus extends HeroicCustomEnchantment {
    public HeroicCactus() {
        super("Mighty Cactus", "Cactus");
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (player instanceof Player) {
/*            if (player.hasMetadata("noDefenseProcs")) {
                return;
            }*/
            if (player.hasMetadata("neutralized")) {
                return;
            }
            final Player p = (Player) player;
            if (attacker != null && attacker instanceof Player) {
                final Player pAttacker = (Player) attacker;
                //pAttacker.damage(0.5 * level);
                RageStacksCache rageStacksCache = CacheUtils.getRageStackCache((Player) pAttacker);
                rageStacksCache.resetRageStack();

                final Location l = p.getLocation();
                WorldUtil.sendWorldEffect(l.getBlock(), 81, 2001);

            }
            final double thornNegateChance = (level == 1) ? 0.5 : 1.0;
            if (e.getCause() == EntityDamageEvent.DamageCause.THORNS && Math.random() < thornNegateChance) {
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }
}
