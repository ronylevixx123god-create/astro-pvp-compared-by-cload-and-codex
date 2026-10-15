/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.Logger;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

class PlayerEnterMaze
extends RegionEvent {
    public Block mazeLever = null;
    private List<Location> possibleMazeLeverLocations = new ArrayList<Location>();

    public PlayerEnterMaze(Dungeon d, String regionName) {
        super(d, regionName);
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -59.0, 79.0, 183.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -61.0, 79.0, 171.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -74.0, 79.0, 180.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -77.0, 79.0, 208.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -75.0, 79.0, 208.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -57.0, 79.0, 166.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -60.0, 79.0, 176.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -81.0, 79.0, 189.0));
        this.possibleMazeLeverLocations.add(new Location(d.getWorld(), -81.0, 79.0, 181.0));
    }

    @Override
    public void run() {
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "This is the ship's generator room. The exit seems to have been sealed in the chaos, you will need to locate the correct lever to switch the power back on and continue. Be careful! The floor seems extremely unstable.");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 240000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Complete Maze");
        this.mazeLever = this.getRandomMazeLever();
        this.mazeLever.setTypeIdAndData(Material.LEVER.getId(), (byte)5, false);
        Logger.debug("mazeLever: " + (Object)this.mazeLever);
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)new Location(this.dungeon.getWorld(), -74.0, 80.5, 172.5), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "LOCKED", (Object)ChatColor.GRAY + "Find the lever!"}));
    }

    private Block getRandomMazeLever() {
        return this.dungeon.getWorld().getBlockAt(this.possibleMazeLeverLocations.get(new Random().nextInt(this.possibleMazeLeverLocations.size())));
    }
}

