/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.cosmicpvp.CosmicSpaceChests.cmds;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFlare
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("/flare <1-6> <player>");
            return true;
        }
        ItemRarity ir = CosmicSpaceChestsAPI.getItemRarity(Integer.parseInt(args[0]));
        Player p = Bukkit.getPlayer((String)args[1]);
        if (p == null) {
            sender.sendMessage((Object)ChatColor.RED + "No player '" + args[1] + "' online.");
            return true;
        }
        ItemUtils.giveItemToPlayer(p, CosmicSpaceChestsAPI.getSpaceFlare(ir));
        sender.sendMessage((Object)ChatColor.YELLOW + "Added " + ir.name + " Space Flare to " + p.getName() + "'s me.astropvp.cratekeys.inventory.");
        return true;
    }
}

