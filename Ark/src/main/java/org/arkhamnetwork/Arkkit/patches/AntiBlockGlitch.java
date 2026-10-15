/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AntiBlockGlitch
        extends ArkPlugin {
    public AntiBlockGlitch(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new AntiWitherSpawnerListener());
        //this.registerListener(new AntiGlitchSwordListener());

    }

    @Override
    public void disable() {
    }
}

