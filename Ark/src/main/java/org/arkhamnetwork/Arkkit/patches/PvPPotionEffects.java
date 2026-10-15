/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PvPPotionEffects
        extends ArkPlugin {
    protected static WorldGuardPlugin wg;

    public PvPPotionEffects(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        wg = this.getWorldGuard();
        this.registerTask(new PvPPotionEffectsTask().runTaskTimer(Arkkit.get(), 30L, 30L), PvPPotionEffects.class);
    }

    @Override
    public void disable() {
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}

