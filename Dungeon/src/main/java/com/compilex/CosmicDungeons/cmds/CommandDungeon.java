/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.cmds;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDungeon
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            Player pSender = (Player)sender;
            pSender.openInventory(CosmicDungeonsPlugin.get_().getDungeonMenuListener().getDungeonMenu(pSender));
            pSender.playSound(pSender.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.8f);
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("givekey")) {
            if (!sender.isOp()) {
                return true;
            }
            String pName = args[1];
            String dungeon = args[2];
            Dungeon d = DungeonsAPI.get().getDungeonByWorldName(dungeon);
            if (d != null) {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("lp user " + pName + " permission set " + d.getPermissionNode()));
                sender.sendMessage((Object)ChatColor.YELLOW + "* Added permission node " + d.getPermissionNode() + " to " + pName + "!");
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Dungeon '" + dungeon + "' could not be found.");
                sender.sendMessage((Object)ChatColor.GRAY + "valid: " + DungeonsAPI.get().getDungeons());
                return true;
            }
        }
        return true;
    }
}

