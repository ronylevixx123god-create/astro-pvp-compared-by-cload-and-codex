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

public class HitMe
        extends ArkPlugin {
    public HitMe(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandHitMe("hitme", "/<command>", "Deals 1/2 heart of damage to command executor.", new ArrayList<String>(Arrays.asList("hurtme"))));
    }

    @Override
    public void disable() {
    }
}

