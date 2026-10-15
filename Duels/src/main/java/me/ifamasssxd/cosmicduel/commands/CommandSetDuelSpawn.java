/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicduel.commands;

import me.ifamasssxd.cosmicduel.struct.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetDuelSpawn
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if (!p.isOp()) {
            return true;
        }
        if (args.length == 1) {
            String arenaname = args[0];
            Arena arena = Arena.getArena(arenaname);
            if (arena == null) {
                p.sendMessage(ChatColor.RED + "Invalid arena. Valid arenas:");
                for (Arena ar : Arena.arenas) {
                    p.sendMessage(ChatColor.YELLOW + ar.getName());
                }
                return true;
            }
            arena.spawnLocations.add(p.getLocation());
            arena.saveSpawns();
            p.sendMessage(ChatColor.RED + "Spawn saved! There are now " + arena.spawnLocations.size() + " Spawns.");
            p.sendMessage(ChatColor.GRAY + "You can remove a spawn with /removeduelspawn");
        } else {
            p.sendMessage(ChatColor.RED + "Correct usage: /setduelspawn <arenaName>");
        }
        return false;
    }
}

