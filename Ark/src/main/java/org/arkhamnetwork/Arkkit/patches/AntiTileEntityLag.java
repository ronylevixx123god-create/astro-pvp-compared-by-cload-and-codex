/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.World
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.BrewingStand
 *  org.bukkit.block.Chest
 *  org.bukkit.material.EnderChest
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.material.EnderChest;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class AntiTileEntityLag
        extends ArkPlugin {
    protected static HashSet<String> scannedChunks;

    public AntiTileEntityLag(Plugin p) {
        super(p);
    }

    protected static void purgeBrewingStands() {
        for (World w : Bukkit.getWorlds()) {
            for (Chunk c : w.getLoadedChunks()) {
                String cs = c.getWorld().getName() + "," + c.getX() + "," + c.getZ();
                if (scannedChunks.contains(cs)) continue;
                scannedChunks.add(cs);
                int brewingStands = AntiTileEntityLag.getBrewingstandCount(c.getTileEntities());
                int chests = AntiTileEntityLag.getChestCount(c.getTileEntities());
                int enderChests = AntiTileEntityLag.getEnderChestCount(c.getTileEntities());
                if (brewingStands > 50) {
                    Bukkit.getLogger().info("(AntiTileEntityLag) Found " + brewingStands + "x Brewing Stands at " + cs + "!");
                }
                if (chests >= 100) {
                    Bukkit.getLogger().info("(AntiTileEntityLag) Found " + chests + "x Chests at " + cs + "!");
                }
                if (enderChests < 100) continue;
                Bukkit.getLogger().info("(AntiTileEntityLag) Found " + enderChests + "x Enderchests at " + cs + "!");
            }
        }
    }

    private static int getBrewingstandCount(BlockState[] blockStates) {
        int x = 0;
        for (BlockState bs : blockStates) {
            if (!(bs instanceof BrewingStand)) continue;
            ++x;
        }
        return x;
    }

    private static int getChestCount(BlockState[] blockStates) {
        int x = 0;
        for (BlockState bs : blockStates) {
            if (!(bs instanceof Chest)) continue;
            ++x;
        }
        return x;
    }

    private static int getEnderChestCount(BlockState[] blockStates) {
        int x = 0;
        for (BlockState bs : blockStates) {
            if (!(bs instanceof EnderChest)) continue;
            ++x;
        }
        return x;
    }

    @Override
    public void enable() {
        scannedChunks = new HashSet();
        AntiTileEntityLag.purgeBrewingStands();
        this.registerTask(new AntiTileEntityLagTask().runTaskTimer(Arkkit.get(), 200L, 200L), this.getClass());
    }

    @Override
    public void disable() {
        scannedChunks.clear();
    }
}

