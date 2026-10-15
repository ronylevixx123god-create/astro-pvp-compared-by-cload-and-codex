/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class TWAutoCraftTNT
        extends ArkPlugin {
    protected static double sandPrice = 1.53;

    public TWAutoCraftTNT(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (ArkPlugins.AUTO_CRAFT_TNT.getArkPlugin().isRunning()) {
            Bukkit.getLogger().info("[Arkkit] AUTO_CRAFT_TNT is already running, cannot run both patches concurrently.");
            this.kill();
            return;
        }
        sandPrice = Arkkit.get().getConfig().getDouble("settings.auto_craft_tnt.sandPrice");
        this.registerCommand(new TWCommandTNT("tnt", "/<command>", "Automatically crafts all TNT ingredients in inventory.", new ArrayList<String>(Arrays.asList("crafttnt"))));
    }

    @Override
    public void disable() {
    }
}

