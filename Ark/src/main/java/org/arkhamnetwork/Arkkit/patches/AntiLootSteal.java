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
import java.util.List;

public class AntiLootSteal
        extends ArkPlugin {
    protected static int itemPickUpDelayInSeconds;
    protected static List<String> disabledWorlds;

    public AntiLootSteal(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        itemPickUpDelayInSeconds = 60;
        disabledWorlds = new ArrayList<String>(Arrays.asList("world_koth"));
        this.registerListener(new AntiLootStealListener());
    }

    @Override
    public void disable() {
    }
}

