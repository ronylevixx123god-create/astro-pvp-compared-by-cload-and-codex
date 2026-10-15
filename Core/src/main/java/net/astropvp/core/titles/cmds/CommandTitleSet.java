/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package net.astropvp.core.titles.cmds;

import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class CommandTitleSet
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("Incorrect usage. Correct usage: /titleset <player> <title>");
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage("Player must be online in order to perform this command on them.");
            return true;
        }
        UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
        String t = args[1];
        AstroCore.getInstance().getDataManager().setEquippedTitle(uuid, t);
        return true;
    }
}

