/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.cmds;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDungeonGive
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("cosmicdungeons.dungeongive")) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(CC.translate("&c/dungeongive <player> <item> <amount>"));
            sender.sendMessage(CC.translate("&cItems: ABANDONED_SHIP_PORTAL, DESTROYED_OUTPOST_PORTAL, PLANET_NUL_PORTAL"));
            return true;
        }
        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[0]);
            return true;
        }
        return true;
    }
}

