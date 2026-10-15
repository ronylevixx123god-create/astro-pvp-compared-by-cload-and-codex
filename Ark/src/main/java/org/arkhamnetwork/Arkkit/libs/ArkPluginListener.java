/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.event.Listener
 */
package org.arkhamnetwork.Arkkit.libs;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.event.Listener;

public abstract class ArkPluginListener<T extends ArkPlugin>
        implements Listener {
    protected T patch;

    public ArkPluginListener(T patch) {
        this.patch = patch;
    }
}

