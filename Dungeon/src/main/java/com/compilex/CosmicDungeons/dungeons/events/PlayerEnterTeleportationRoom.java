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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

class PlayerEnterTeleportationRoom
extends RegionEvent {
    public LinkedList<Location> buttonLocations;
    public LinkedList<Location> currentButtons = new LinkedList();
    public Block teleportLever = null;
    public HashMap<Location, ChatColor> buttonColors;

    public PlayerEnterTeleportationRoom(Dungeon d, String regionName) {
        super(d, regionName);
        this.buttonLocations = new LinkedList();
        this.buttonColors = new HashMap();
        this.buttonLocations.add(new Location(d.getWorld(), -235.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -237.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -239.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -240.0, 70.0, 198.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -240.0, 70.0, 196.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -240.0, 70.0, 185.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -240.0, 70.0, 183.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -239.0, 70.0, 182.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -237.0, 70.0, 182.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(d.getWorld(), -235.0, 70.0, 182.0).getBlock().getLocation());
        Collections.shuffle(this.buttonLocations);
        this.buttonColors.put(new Location(d.getWorld(), -235.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.DARK_GRAY);
        this.buttonColors.put(new Location(d.getWorld(), -237.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.LIGHT_PURPLE);
        this.buttonColors.put(new Location(d.getWorld(), -239.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.BLUE);
        this.buttonColors.put(new Location(d.getWorld(), -240.0, 70.0, 198.0).getBlock().getLocation(), ChatColor.GRAY);
        this.buttonColors.put(new Location(d.getWorld(), -240.0, 70.0, 196.0).getBlock().getLocation(), ChatColor.RED);
        this.buttonColors.put(new Location(d.getWorld(), -240.0, 70.0, 185.0).getBlock().getLocation(), ChatColor.GOLD);
        this.buttonColors.put(new Location(d.getWorld(), -240.0, 70.0, 183.0).getBlock().getLocation(), ChatColor.DARK_PURPLE);
        this.buttonColors.put(new Location(d.getWorld(), -239.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.AQUA);
        this.buttonColors.put(new Location(d.getWorld(), -237.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.YELLOW);
        this.buttonColors.put(new Location(d.getWorld(), -235.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.GREEN);
        this.teleportLever = new Location(d.getWorld(), -236.0, 71.0, 191.0).getBlock();
    }

    @Override
    public void run() {
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "Yijki is not here! He must have beamed down to the planet. Opperate the teleportation device and escape the ship before it falls apart!");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 300000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Solve Teleportation Room Puzzle");
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "To use the teleportor, you must operate all 10 buttons in the correct order, then pull the lever in the center of the room.");
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)this.teleportLever.getLocation().add(0.0, 2.0, 0.5), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "LOCKED", (Object)ChatColor.GRAY + "Solve puzzle to complete dungeon."}));
        int x = 0;
        for (Location l : this.buttonLocations) {
            Logger.debug("Button #" + ++x + ": " + (Object)l + " (" + this.buttonColors.get((Object)l).getChar() + ")");
        }
        Logger.debug("teleport_lever: " + (Object)this.teleportLever.getLocation());
        this.teleportLever.setTypeIdAndData(Material.LEVER.getId(), (byte)5, false);
    }
}

