/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandBlockCommand
        extends AbstractCommand {
    public CommandBlockCommand(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockcommand")) {
            if (sender instanceof Player && !sender.isOp()) {
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("Usage: /blockcommand <add/remove/list> <command>");
                return true;
            }
            String opperation = args[0];
            String command = "";
            for (int i = 1; i < args.length; ++i) {
                command = command + args[i] + " ";
            }
            command = command.trim();
            if (opperation.equalsIgnoreCase("add")) {
                CommandBlocker.blockCommand(command);
                sender.sendMessage(ChatColor.RED + "Command '" + command + "' is now blocked.");
            } else if (opperation.equalsIgnoreCase("remove")) {
                if (CommandBlocker.unblockCommand(command)) {
                    sender.sendMessage(ChatColor.GREEN + "Command '" + command + "' is now unblocked.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Oops! Something went wrong while attempting to unblock the command '" + command + "', check console for errors.");
                }
            } else if (opperation.equalsIgnoreCase("list")) {
                for (String blocked : CommandBlocker.getBlockedCommands()) {
                    sender.sendMessage(ChatColor.RED + "/" + blocked + " - BLOCKED");
                }
                for (String blocked_alias : CommandBlocker.getBlockedAliases()) {
                    sender.sendMessage(ChatColor.YELLOW + "/" + blocked_alias + " - ALIAS");
                }
            }
        }
        return true;
    }
}

