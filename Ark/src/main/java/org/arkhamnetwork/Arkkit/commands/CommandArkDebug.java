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
package org.arkhamnetwork.Arkkit.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.Arkkit.StaticMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandArkDebug implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("arkkit.command.admin")) {
            sender.sendMessage(StaticMessages.NO_PERMISSION);
            return true;
        }
        sender.sendMessage(CC.Gray + CC.Strike + "---------------------------------");
        sender.sendMessage(CC.Gold + "Arkkit is loaded and net.astropvp.armorsets.commands should be available.");
        sender.sendMessage(CC.Gold + "Ensure the user has 'arkkit.command.admin'");
        sender.sendMessage(CC.Gray + CC.Strike + "---------------------------------");
        return true;
    }
}

