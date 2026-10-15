/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.gmail.nossr50.datatypes.player.PlayerProfile
 *  com.gmail.nossr50.util.player.UserManager
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SafeShutdown
        extends ArkPlugin {
    public SafeShutdown(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
        if (Arkkit.get().shutting_down) {
            boolean hasWhitelist = Bukkit.hasWhitelist();
            Bukkit.setWhitelist(true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer("The server is rebooting!");
            }
            for (World w : Bukkit.getWorlds()) {
                w.save();
                int size = w.getLoadedChunks().length;
                for (Chunk c : w.getLoadedChunks()) {
                    String s = c.getX() + "," + c.getZ();
                    c.unload(true, true);
                    Bukkit.getLogger().info("Unloaded (" + w.getName() + ") " + s + ", " + size-- + " chunks left.");
                }
            }
            if (!hasWhitelist) {
                Bukkit.setWhitelist(false);
            }
        }
    }
}

