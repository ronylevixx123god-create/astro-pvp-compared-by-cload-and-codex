/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class NoNewChunks
        extends ArkPlugin {
    protected static List<String> worlds;

    public NoNewChunks(Plugin p) {
        super(p);
        worlds = new ArrayList<String>();
    }

    @Override
    public void enable() {
        worlds = this.getBukkitPlugin().getConfig().getStringList("settings.no_new_chunks.worlds");
        this.registerListener(new NoNewChunksListener());
    }

    @Override
    public void disable() {
    }
}

