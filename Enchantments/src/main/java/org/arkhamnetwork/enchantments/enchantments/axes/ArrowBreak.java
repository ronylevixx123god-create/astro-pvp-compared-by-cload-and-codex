/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ArrowBreak
extends CustomEnchantment {
    public ArrowBreak() {
        super("Arrow Break", GeneralUtil.axe, 2);
        this.max = 6;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        EntityDamageByEntityEvent edbee;
        if (e instanceof EntityDamageByEntityEvent && (edbee = (EntityDamageByEntityEvent)e).getDamager() instanceof Projectile && Math.random() < (0.1 + 0.04 * (double)level) / 2.0) {
            try {
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            ((Player)player).playSound(player.getLocation(), Sound.ITEM_BREAK, 0.7f, 0.2f);
            e.setCancelled(true);
            edbee.getDamager().remove();
        }
    }
}

