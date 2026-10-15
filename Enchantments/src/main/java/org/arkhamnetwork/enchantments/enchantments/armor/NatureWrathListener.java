/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

final class NatureWrathListener
implements Listener {
    NatureWrathListener() {
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && ((Player)e.getDamager()).getWalkSpeed() == 0.0f) {
            try {
                //ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), e.getEntity().getLocation(), 0.6f, 0.6f, 0.6f, 0.1f, 30);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (e.getEntity() instanceof Player && (p = (Player)e.getEntity()).hasMetadata("natureWrathTask")) {
            NatureWrathTask nwt = (NatureWrathTask)((Object)((MetadataValue)p.getMetadata("natureWrathTask").get(0)).value());
            if (Math.random() < 0.3 - 0.075 * (double)nwt.enchantLevel) {
                nwt.cancel();
                p.setWalkSpeed(0.2f);
                p.removeMetadata("natureWrathTask", (Plugin)Enchantments.getInstance());
                try {
                    //ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), e.getEntity().getLocation(), 0.6f, 0.6f, 0.6f, 0.1f, 30);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }            }
        }
    }

    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && ((Player)e.getEntity()).getWalkSpeed() == 0.0f) {
            try {
                //ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), e.getEntity().getLocation(), 0.6f, 0.6f, 0.6f, 0.1f, 30);
            } catch (Exception exception) {
                exception.printStackTrace();
            }        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent e) {
        if (e.getPlayer().hasMetadata("natureWrathTask")) {
            NatureWrathTask nwt = (NatureWrathTask)((Object)((MetadataValue)e.getPlayer().getMetadata("natureWrathTask").get(0)).value());
            nwt.cancel();
            e.getPlayer().removeMetadata("natureWrathTask", (Plugin)Enchantments.getInstance());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (e.getPlayer().getWalkSpeed() == 0.0f) {
            e.getPlayer().setWalkSpeed(0.2f);
        }
        if (e.getPlayer().hasMetadata("natureWrathTask")) {
            NatureWrathTask nwt = (NatureWrathTask)((Object)((MetadataValue)e.getPlayer().getMetadata("natureWrathTask").get(0)).value());
            nwt.cancel();
            e.getPlayer().removeMetadata("natureWrathTask", (Plugin)Enchantments.getInstance());
        }
    }
}

