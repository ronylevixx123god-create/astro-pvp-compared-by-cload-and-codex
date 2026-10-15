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
import java.util.Arrays;

public class Bump
        extends ArkPlugin {
    public Bump(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new BumpListener());
        this.registerCommand(new CommandBump("bump", "/<command>", "Deals 1/2 heart of damage to command executor.", new ArrayList<String>(Arrays.asList("bumpme"))));
    }

    @Override
    public void disable() {
    }
}

