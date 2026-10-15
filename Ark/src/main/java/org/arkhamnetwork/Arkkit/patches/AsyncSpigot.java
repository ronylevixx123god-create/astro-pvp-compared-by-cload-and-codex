/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.spigotmc.AsyncCatcher
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.spigotmc.AsyncCatcher;

public class AsyncSpigot
        extends ArkPlugin {
    public AsyncSpigot(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        Bukkit.getLogger().info("Disabling spigot's AsyncCatcher!");
        AsyncCatcher.enabled = false;
        this.registerListener(new DebugListener());
    }

    @Override
    public void disable() {
        Bukkit.getLogger().info("Enabling spigot's AsyncCatcher!");
        AsyncCatcher.enabled = true;
    }
}

