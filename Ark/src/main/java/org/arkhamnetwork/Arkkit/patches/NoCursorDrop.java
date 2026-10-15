/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

public class NoCursorDrop
        extends ArkPlugin {
    public NoCursorDrop(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new NoCursorDropListener());
    }

    @Override
    public void disable() {
    }
}

