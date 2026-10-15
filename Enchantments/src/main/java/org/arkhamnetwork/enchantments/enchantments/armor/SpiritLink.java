package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import java.util.*;

public class SpiritLink extends CustomEnchantment
{
    public SpiritLink() {
        super("Spirit Link", GeneralUtil.chestplates, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (player.hasMetadata("neutralized")) {
            return;
        }
        if (player.hasMetadata("noDefenseProcs")) {
            return;
        }
        final double chance = level * 0.0075;
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && Math.random() < chance) {
            final Player playerHurt = (Player)player;
            final double healAmount = Math.min(4.0, e.getDamage() / (this.max - (level - 1)) / 4.0);
            final double radius = level * 2;
            for (final Entity ent : player.getNearbyEntities(radius, radius, radius)) {
                if (ent instanceof Player) {
                    final Player playerNear = (Player)ent;
                    if (!this.isAlly(playerHurt, playerNear)) {
                        continue;
                    }
                    GeneralUtil.healPlayer(playerNear, healAmount);                }
            }
            try {
                ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 1, 1, 1, 0.05f, 10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.35f);
        }
    }

    private boolean isAlly(final Player p1, final Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}
