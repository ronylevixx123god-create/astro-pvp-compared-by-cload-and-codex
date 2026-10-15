/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package me.ifamasssxd.cosmicduel.commands;

import me.ifamasssxd.cosmicduel.struct.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandSetDuelWorld
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: /setduelworld <name>");
            return true;
        }
        String world = args[0];
        int changed = 0;
        for (Arena arena : Arena.arenas) {
            List<Location> spawns = arena.getSpawnLocations();
            for (Location l : spawns) {
                if (l.getWorld() == null || l.getWorld() != Bukkit.getWorld((String)world)) continue;
                l.setWorld(Bukkit.getWorld((String)world));
                ++changed;
            }
            arena.setSpawns(spawns);
            arena.saveSpawns();
        }
        sender.sendMessage(ChatColor.RED.toString() + changed + " spawns converted to world " + world);
        return true;
    }
}

