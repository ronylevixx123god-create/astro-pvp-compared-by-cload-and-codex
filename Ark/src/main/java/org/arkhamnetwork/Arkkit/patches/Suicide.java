/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class Suicide
        extends ArkPlugin {
    public Suicide(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandSuicide("suicide", "/<command>", "Kills and respawns your character.", Arrays.asList("killme")));
        this.registerListener(new SuicideListener());
    }

    @Override
    public void disable() {
    }
}

