/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import de.inventivegames.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

final class NatureWrathTask
extends BukkitRunnable {
    Player victim;
    public int enchantLevel;

    public NatureWrathTask(Player p, int level) {
        this.victim = p;
        this.enchantLevel = level;
    }

    public void run() {
        if (this.victim != null && !this.victim.isDead()) {
            this.victim.getWorld().strikeLightningEffect(this.victim.getLocation());
            this.victim.damage((double)this.enchantLevel);
            this.victim.playSound(this.victim.getLocation(), Sound.GHAST_SCREAM2, 2.0f, 2.0f);
            this.victim.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "** NATURE'S WRATH **");
            try {
                //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), this.victim.getLocation().add(0.0, 1.0, 0.0), 0.4f, 0.4f, 0.4f, 0.1f, 35);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

