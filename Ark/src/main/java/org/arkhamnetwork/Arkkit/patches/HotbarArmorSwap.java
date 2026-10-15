/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class HotbarArmorSwap
        extends ArkPlugin {
    protected static boolean masksEnabled = false;

    public HotbarArmorSwap(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        masksEnabled = Bukkit.getPluginManager().isPluginEnabled("CosmicMasks");
        this.registerListener(new HotbarArmorSwapListener());
    }

    @Override
    public void disable() {
    }
}

