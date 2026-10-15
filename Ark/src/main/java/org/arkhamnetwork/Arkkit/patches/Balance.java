/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.IEssentials
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.IEssentials;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Balance
        extends ArkPlugin {
    protected static IEssentials ess3;

    public Balance(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            Bukkit.getLogger().info("[Arkkit (Balance)] Disabling Near patch due to no Essentials.jar loaded.");
            this.kill();
            return;
        }
        ess3 = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        this.registerCommand(new CommandBalance("balance", "/<command> [player]", "States the current balance of a player.", new ArrayList<>(Arrays.asList("cbal", "bal", "ebal", "ebalance", "money", "emoney"))));
    }

    @Override
    public void disable() {
        ess3 = null;
    }
}

