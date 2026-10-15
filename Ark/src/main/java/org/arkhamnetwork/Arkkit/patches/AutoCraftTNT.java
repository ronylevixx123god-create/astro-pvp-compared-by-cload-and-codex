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

public class AutoCraftTNT
        extends ArkPlugin {
    protected static double sandPrice = 3.05;

    public AutoCraftTNT(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (ArkPlugins.TW_AUTO_CRAFT_TNT.getArkPlugin().isRunning()) {
            Bukkit.getLogger().info("[Arkkit] TW_AUTO_CRAFT_TNT is already running, cannot run both patches concurrently.");
            this.kill();
            return;
        }
        sandPrice = Arkkit.get().getConfig().getDouble("settings.auto_craft_tnt.sandPrice");
        this.registerCommand(new CommandTNT("tnt", "/<command>", "Automatically crafts all TNT ingredients in inventory."));
    }

    @Override
    public void disable() {
    }
}

