/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class EssentialsWarpAliases
        extends ArkPlugin {
    public EssentialsWarpAliases(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (!this.getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("Essentials")) {
            Bukkit.getLogger().info("(EssentialsWarpAliases) Could not find Essentials.jar dependancy, disabling patch.");
            this.kill();
            return;
        }
        Essentials ess = Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials") ? (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials") : null;
        for (String warp : ess.getWarps().getList()) {
            if (Bukkit.getCommandAliases().containsKey(warp)) {
                Bukkit.getLogger().info("Skipped creating command alias of /warp" + warp + " due to existing command.");
                continue;
            }
            Bukkit.getLogger().info("Creating command alias of /" + warp + " for /warp " + warp + "!");
        }
        this.registerListener(new EssentialsWarpAliasesListener());
    }

    @Override
    public void disable() {
    }
}

