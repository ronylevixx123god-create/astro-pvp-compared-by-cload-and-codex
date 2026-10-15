/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Gamble
        extends ArkPlugin {
    protected static HashSet<String> disabledRollUUIDs = new HashSet();

    public Gamble(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (Arkkit.get().getConfig().contains("settings.gamble.disabledRolUUIDs")) {
            for (String s : Arkkit.get().getConfig().getStringList("settings.gamble.disabledRollUUIDs")) {
                disabledRollUUIDs.add(s);
            }
        }
        this.registerCommand(new CommandRoll("roll", "/<command> <1-10000>", "Rolls a random number between 0 and X", Collections.emptyList()));
    }

    @Override
    public void disable() {
        this.unregisterCommands();
        Arkkit.get().getConfig().set("settings.gamble.disabledRollUUIDs", disabledRollUUIDs);
    }
}

