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

public class NearLegacy
        extends ArkPlugin {
    protected static IEssentials ess3;

    public NearLegacy(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {

        ess3 = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        this.registerCommand(new CommandNear("near", "/<command> [playername] [radius]", "Lists the players near by or around a player.", new ArrayList<String>(Arrays.asList("cnear", "enear", "nearby", "enearby"))));
    }

    @Override
    public void disable() {
        ess3 = null;
    }
}

