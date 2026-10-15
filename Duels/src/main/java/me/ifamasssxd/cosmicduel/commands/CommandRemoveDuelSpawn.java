/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicduel.commands;

import me.ifamasssxd.cosmicduel.struct.Arena;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveDuelSpawn
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if (!p.isOp()) {
            return true;
        }
        if (args.length == 2) {
            String arenaname = args[0];
            Arena arena = Arena.getArena(arenaname);
            if (StringUtils.isNumeric((CharSequence)args[1])) {
                int index = Integer.parseInt(args[1]);
                if (arena == null) {
                    p.sendMessage((Object)ChatColor.RED + "Invalid arena. Valid arenas:");
                    for (Arena ar : Arena.arenas) {
                        p.sendMessage((Object)ChatColor.YELLOW + ar.getName());
                    }
                    return true;
                }
                if (arena.spawnLocations.size() <= index) {
                    p.sendMessage((Object)ChatColor.RED + "Invalid spawn index.");
                    return true;
                }
                arena.spawnLocations.remove(index);
                arena.saveSpawns();
                p.sendMessage((Object)ChatColor.RED + "Spawn removed! There are now " + arena.spawnLocations.size() + " Spawns.");
                p.sendMessage((Object)ChatColor.GRAY + "You can add a spawn with /setduelspawn <arena>");
            } else {
                p.sendMessage((Object)ChatColor.RED + "Please enter a valid number as an index.");
            }
        } else if (args.length == 1) {
            String arenaname = args[0];
            Arena arena = Arena.getArena(arenaname);
            if (arena == null) {
                p.sendMessage((Object)ChatColor.RED + "Invalid arena. Valid arenas:");
                for (Arena ar2 : Arena.arenas) {
                    p.sendMessage((Object)ChatColor.YELLOW + ar2.getName());
                }
                return true;
            }
            int index = 0;
            for (Location l : arena.spawnLocations) {
                p.sendMessage(ChatColor.YELLOW.toString() + index++ + ". " + (Object)ChatColor.RED + l.getWorld().getName() + " X: " + l.getX() + " Y: " + l.getY() + " Z: " + l.getZ() + " Pitch: " + l.getPitch() + " Yaw: " + l.getYaw());
            }
            p.sendMessage((Object)ChatColor.RED + "Remove a spawn with /removeduelspawn <arena> <index>");
        } else {
            p.sendMessage((Object)ChatColor.RED + "Correct usage: /removeduelspawn <arenaName>");
        }
        return false;
    }
}

