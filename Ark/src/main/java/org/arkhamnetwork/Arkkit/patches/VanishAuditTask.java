/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.User
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.User;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class VanishAuditTask
        extends BukkitRunnable {
    private final List<String> playersToWatch = new ArrayList<String>(Arrays.asList("Vaquxine"));


    public void run() {
    }

    final class VanishAuditListener
            implements Listener {
        VanishAuditListener() {
        }

        @EventHandler
        public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
            if (VanishAuditTask.this.playersToWatch.contains(e.getPlayer().getName()) && (e.getMessage().equals("/vanish") || e.getMessage().equals("/evanish"))) {
                User u = EssentialsUtils.getEssentials().getUser(e.getPlayer());
                Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> e.getPlayer().setMetadata("trackedVanishState", new FixedMetadataValue(Arkkit.get(), u.isVanished())), 1L);
            }
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            if (e.getPlayer().hasPermission("arkkit.staff.hidden")) {
                VanishAuditTask.this.playersToWatch.add(e.getPlayer().getName());
            }
            if (VanishAuditTask.this.playersToWatch.contains(e.getPlayer().getName())) {
                e.getPlayer().setMetadata("trackedVanishState", new FixedMetadataValue(Arkkit.get(), true));
            }
        }
    }
}

