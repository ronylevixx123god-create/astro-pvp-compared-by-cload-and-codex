/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.rit.sucy.service;

import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.service.ICommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandHandler
implements CommandExecutor {
    private final Map<String, ICommand> registeredCommands = new HashMap<String, ICommand>();
    private final Map<String, CommandHandler> registeredHandlers = new HashMap<String, CommandHandler>();
    protected final EnchantmentAPI plugin;
    private final String cmd;

    protected CommandHandler(EnchantmentAPI plugin, String cmd) {
        this.plugin = plugin;
        this.cmd = cmd;
    }

    protected void registerCommand(String label, ICommand command) {
        if (this.registeredCommands.containsKey(label)) {
            this.plugin.getLogger().warning("Replacing existing command for: " + label);
        }
        this.registeredCommands.put(label, command);
    }

    public void unregisterCommand(String label) {
        this.registeredCommands.remove(label);
    }

    public void registerHandler(CommandHandler handler) {
        if (this.registeredHandlers.containsKey(handler.getCommand())) {
            this.plugin.getLogger().warning("Replacing existing handler for: " + handler.getCommand());
        }
        this.registeredHandlers.put(handler.getCommand(), handler);
    }

    public void unregisterHandler(String label) {
        this.registeredHandlers.remove(label);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return this.noArgs(sender, command, label);
        }
        String subcmd = args[0].toLowerCase();
        CommandHandler handler = this.registeredHandlers.get(subcmd);
        if (handler != null) {
            return handler.onCommand(sender, command, label, this.shortenArgs(args));
        }
        ICommand subCommand = this.registeredCommands.get(subcmd);
        if (subCommand == null) {
            return this.unknownCommand(sender, command, label, args);
        }
        boolean value = true;
        try {
            value = subCommand.execute(this.plugin, sender, command, label, this.shortenArgs(args));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage((Object)ChatColor.GRAY + this.plugin.getTag() + (Object)ChatColor.RED + " Missing parameters.");
        }
        return value;
    }

    protected abstract boolean noArgs(CommandSender var1, Command var2, String var3);

    protected abstract boolean unknownCommand(CommandSender var1, Command var2, String var3, String[] var4);

    String[] shortenArgs(String[] args) {
        if (args.length == 0) {
            return args;
        }
        ArrayList<String> argList = new ArrayList<String>();
        for (int i = 1; i < args.length; ++i) {
            argList.add(args[i]);
        }
        return argList.toArray(new String[argList.size()]);
    }

    String getCommand() {
        return this.cmd;
    }
}

