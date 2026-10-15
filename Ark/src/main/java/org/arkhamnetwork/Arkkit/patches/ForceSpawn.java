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
import java.util.Collections;

public class ForceSpawn
        extends ArkPlugin {
    public ForceSpawn(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandForceSpawn("forcespawn", "/<command> [args]", "Forces all players in a radius to be teleported to the main world's spawn.", new ArrayList<String>(Collections.singletonList("sendtospawn"))));
    }

    @Override
    public void disable() {
    }
}

