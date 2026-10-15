/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches.createtitle;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

public class CreateTitle
        extends ArkPlugin {
    public CreateTitle(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandCreateTitle("createtitle", "/<command>", "Creates a title redeem item."));
    }

    @Override
    public void disable() {
    }
}

