/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.commands;

import com.cosmicpvp.cosmicutils.struct.log.CosmicLog;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCosmicDebug
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("uuid")) {
            CosmicLog.debugUUID = !CosmicLog.debugUUID;
            sender.sendMessage(CC.Red + "UUID Debug: " + CosmicLog.debugUUID + "!");
            return true;
        }
        CosmicLog.debugEnabled = !CosmicLog.debugEnabled;
        sender.sendMessage(CC.Red + "Debugging: " + CosmicLog.debugEnabled);
        return false;
    }
}

