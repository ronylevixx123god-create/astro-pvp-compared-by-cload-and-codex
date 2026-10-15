/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.DurabilityUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class Hardened
extends CustomEnchantment {
    public Hardened() {
        super("Hardened", GeneralUtil.armor, 2);
        this.max = 3;
        this.base = 17.0;
        this.interval = 6.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double chance;
        double random = Math.random();
        if (random < (chance = 0.2 * (double)level)) {
            DurabilityUtil.healMostDamagedArmorPeice((Player)player, (short)1);
            Location l = player.getLocation();
            try {
                //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), l, 1.0f, 1.0f, 1.0f, 0.1f, 4);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

