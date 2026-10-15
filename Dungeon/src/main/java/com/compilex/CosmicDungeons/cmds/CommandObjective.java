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
package com.compilex.CosmicDungeons.cmds;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandObjective
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player pSender = (Player)sender;
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(pSender);
            if (dp.getCurrentDungeon() == null) {
                pSender.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You are not currently in a /dungeon!");
                return true;
            }
            Dungeon d = dp.getCurrentDungeon();
            if (d.getTimeTask() == null || d.getTimeTask().getObjectiveName() == null) {
                pSender.sendMessage((Object)ChatColor.YELLOW + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "No active objective in this dungeon!");
                return true;
            }
            pSender.sendMessage((Object)ChatColor.GOLD + "OBJECTIVE: " + (Object)ChatColor.YELLOW + (Object)ChatColor.UNDERLINE + d.getTimeTask().getObjectiveName());
            pSender.sendMessage((Object)ChatColor.GOLD + "Time to Complete: " + (Object)ChatColor.GREEN + TimeUtils.formatDifference(d.getTimeTask().getExpireTime() - System.currentTimeMillis()));
        }
        return true;
    }
}

