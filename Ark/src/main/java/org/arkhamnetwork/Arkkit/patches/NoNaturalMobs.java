/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.Plugin;

public class NoNaturalMobs
        extends ArkPlugin {
    public NoNaturalMobs(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        for (World w : Bukkit.getWorlds()) {
            ((CraftWorld) w).getHandle().ticksPerAnimalSpawns = 0L;
            ((CraftWorld) w).getHandle().ticksPerMonsterSpawns = 0L;
        }
        this.registerListener(new NoNaturalMobsListener());
    }

    @Override
    public void disable() {
        for (World w : Bukkit.getWorlds()) {
            ((CraftWorld) w).getHandle().ticksPerAnimalSpawns = 200L;
            ((CraftWorld) w).getHandle().ticksPerMonsterSpawns = 20L;
        }
    }
}

