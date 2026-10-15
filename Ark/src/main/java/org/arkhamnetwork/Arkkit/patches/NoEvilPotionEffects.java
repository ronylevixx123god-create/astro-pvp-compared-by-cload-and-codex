/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

final class NoEvilPotionEffects
        extends BukkitRunnable {
    NoEvilPotionEffects() {
    }

    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPotionEffect(PotionEffectType.BLINDNESS) || !p.hasPermission("essentials.vanish") || !SmarterEssentials.ess3.getUser(p).isVanished())
                continue;
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}

