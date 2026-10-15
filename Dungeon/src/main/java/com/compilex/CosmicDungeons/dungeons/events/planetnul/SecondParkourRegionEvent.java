/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.boydti.fawe.FaweAPI
 *  com.boydti.fawe.object.visitor.FastIterator
 *  com.boydti.fawe.util.EditSessionBuilder
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.visitor.FastIterator;
import com.boydti.fawe.util.EditSessionBuilder;
import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.utils.BlockUtils;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SecondParkourRegionEvent
extends RegionEvent {
    public SecondParkourRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 75000 : 105000), "Complete Volatile Escape");
        final EditSession editSession = new EditSessionBuilder(FaweAPI.getWorld((String)this.dungeon.getWorld().getName())).build();
        new BukkitRunnable(){
            int currentY = 75;

            public void run() {
                if (SecondParkourRegionEvent.this.dungeon == null || SecondParkourRegionEvent.this.dungeon.getPlayers().isEmpty() || SecondParkourRegionEvent.this.dungeon.getWorld() == null || this.currentY >= 140) {
                    this.cancel();
                    return;
                }
                for (Vector pt : new FastIterator(new CuboidRegion(new Vector(766, this.currentY, 603), new Vector(756, this.currentY, 506)), editSession)) {
                    Block block = SecondParkourRegionEvent.this.dungeon.getWorld().getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
                    if (block.getType() != Material.AIR) continue;
                    block.setType(Material.STATIONARY_LAVA);
                }
                ++this.currentY;
            }
        }.runTaskTimer((Plugin)CosmicDungeonsPlugin.get(), 0L, 30L);
    }
}

