/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Board
 *  com.wimbli.WorldBorder.WorldBorder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.struct.factions.FactionHandler;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryBoard;
import com.massivecraft.factions.data.json.JSONBoard;
import com.wimbli.WorldBorder.WorldBorder;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class WildernessCommand extends ArkPlugin {
    protected static int xCenter;
    protected static int zCenter;
    protected static int maxRange;
    protected static int minRange;
    protected static List<String> worlds;
    protected static List<Material> disabledBlocks;
    protected static String messageSuccess;
    protected static String messageFailed;
    protected static String messageWorld;
    //protected static HashSet<String> claimedChunkCoords;

    public WildernessCommand(Plugin p) {
        super(p);
    }

    public static boolean canUseWilderness(Player player) {
        World world = player.getWorld();
        if (worlds != null && !worlds.contains(world.getName())) {
            return false;
        }
        return WorldGuardUtils.isPvPDisabled(player.getLocation());
    }

    @Override
    public void enable() {
        if (!this.getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("Factions")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "(WildernessCommand) Could not enable patch due to lack of Factions.jar!");
            this.kill();
            return;
        }
        //claimedChunkCoords = this.getClaimedChunkCoords();
        xCenter = Arkkit.get().getConfig().getInt("settings.wilderness.x-center");
        zCenter = Arkkit.get().getConfig().getInt("settings.wilderness.z-center");
        maxRange = Arkkit.get().getConfig().getInt("settings.wilderness.max-range");
        minRange = Arkkit.get().getConfig().getInt("settings.wilderness.min-range");
        if (this.getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("WorldBorder") && WorldBorder.plugin.GetWorldBorder("world").getRadiusX() < maxRange) {
            maxRange = WorldBorder.plugin.GetWorldBorder("world").getRadiusX() - 10;
        }
        messageSuccess = ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "You have teleported to a random spot in the wilderness.";
        messageFailed = ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) + " + ChatColor.YELLOW + "No wilderness location found. Try again!";
        messageWorld = ChatColor.RED.toString() + "You cannot use this command in this world.";
        worlds = Arkkit.get().getConfig().getStringList("settings.wilderness.whitelisted-worlds");
        disabledBlocks = new ArrayList<Material>();
        for (String materialName : Arkkit.get().getConfig().getStringList("settings.wilderness.disabled-blocks")) {
            if (Material.valueOf(materialName) == null) continue;
            Bukkit.getLogger().info("[Arkkit (Wilderness)] Adding " + materialName + " to disabledBlocks!");
            disabledBlocks.add(Material.valueOf(materialName));
        }
        this.registerListener(new WildernessListener());
        this.registerCommand(new CommandWilderness("wilderness", "/<command>", "Teleports you to a random location in the wilderness.", new ArrayList<String>(Arrays.asList("wild"))));
    }

    @Override
    public void disable() {
    }


    private HashSet<String> getClaimedChunkCoords() {
        String worldNeeded = Bukkit.getWorlds().get(0).getName();
        HashSet<String> map = new HashSet<String>();
        for (Map.Entry data : Board.getBoardImpl().dumpAsSaveFormat().entrySet()) {
            if (data.getKey() == null || !data.getKey().equals(worldNeeded)) continue;
            map.addAll(((Map) data.getValue()).keySet());
        }
        Bukkit.getLogger().info("(WildernessCommand) Loaded " + map.size() + " claimed chunks into memory!");
        return map;
    }
}

