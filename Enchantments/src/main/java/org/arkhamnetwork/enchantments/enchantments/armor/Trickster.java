/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;

public class Trickster
extends CustomEnchantment {
    public Trickster() {
        super("Trickster", GeneralUtil.armor, 2);
        this.max = 8;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double random = Math.random();
        double chance = 0.0010 * (double)level;
        if (!(attacker instanceof Player)) {
            return;
        }
        if (random < chance) {
            Location l1 = player.getLocation().add(0.0, 1.0, 0.0);
            Location l2 = player.getTargetBlock((HashSet<Byte>) null, 4).getLocation().add(0.0, 1.0, 0.0).setDirection(attacker.getLocation().getDirection());
            if (l2.getBlock().getType() == Material.AIR && l2.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                player.teleport(l2, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                player.getWorld().playSound(l1, Sound.PORTAL_TRIGGER, 0.8f, 1.4f);

            }
        }
    }
}

