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

public class AntiDupe
        extends ArkPlugin {
    protected static boolean mcmmo = false;

    public AntiDupe(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new AntiDupeListener(this));
        this.registerListener(new InventoryScannerListener());
        this.registerListener(new InventoryRatelimitListener());
    }

    @Override
    public void disable() {
    }
}

