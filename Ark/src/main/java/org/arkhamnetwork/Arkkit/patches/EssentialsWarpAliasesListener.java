/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

final class EssentialsWarpAliasesListener
        implements Listener {
    private final Essentials ess = Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials") ? (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials") : null;

    EssentialsWarpAliasesListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreProcessCommand(PlayerCommandPreprocessEvent e) {
        for (String warp : this.ess.getWarps().getList()) {
            if (Bukkit.getCommandAliases().containsKey(e.getMessage().replace("/", ""))) {
                return;
            }

            if (warp.equalsIgnoreCase("end")) {
                continue;
            }

            if (warp.equalsIgnoreCase("arcade")) {
                continue;
            }

            if (warp.equalsIgnoreCase("ruins")) {
                continue;
            }

            if (warp.equalsIgnoreCase("koth")) {
                continue;
            }

            if (warp.equalsIgnoreCase("space")) continue;

            if (!e.getMessage().toLowerCase().equals("/" + warp.toLowerCase())) continue;
            e.setMessage("/warp " + warp);
        }
    }
}

