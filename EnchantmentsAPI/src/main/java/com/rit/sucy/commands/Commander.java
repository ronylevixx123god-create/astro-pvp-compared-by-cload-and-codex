/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.rit.sucy.commands;

import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.commands.AddEnchantCommand;
import com.rit.sucy.commands.BookCommand;
import com.rit.sucy.commands.EnchantListCommand;
import com.rit.sucy.commands.GraphCommand;
import com.rit.sucy.commands.ReloadCommand;
import com.rit.sucy.commands.StatCommand;
import com.rit.sucy.service.CommandHandler;
import com.rit.sucy.service.ICommand;
import com.rit.sucy.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commander
extends CommandHandler {
    public Commander(EnchantmentAPI plugin) {
        super(plugin, "EnchantAPI");
        HelpCommand help = new HelpCommand();
        this.registerCommand("help", help);
        this.registerCommand("?", help);
        this.registerCommand("reload", new ReloadCommand());
        this.registerCommand("list", new EnchantListCommand());
        this.registerCommand("add", new AddEnchantCommand());
        this.registerCommand("book", new BookCommand());
        this.registerCommand("stats", new StatCommand());
        this.registerCommand("graph", new GraphCommand());
    }

    @Override
    public boolean noArgs(CommandSender sender, Command command, String label) {
        sender.sendMessage((Object)ChatColor.GRAY + "========= " + (Object)ChatColor.RED + this.plugin.getName() + (Object)ChatColor.GRAY + " =========");
        sender.sendMessage(" /enchantapi");
        sender.sendMessage("    help         " + (Object)ChatColor.YELLOW + "- Show the help menu");
        if (sender.hasPermission(PermissionNode.LIST.getNode())) {
            sender.sendMessage("    list          " + (Object)ChatColor.YELLOW + "- List all enchantments");
            sender.sendMessage("    list <page> " + (Object)ChatColor.YELLOW + "- List a page of enchantments with descriptions");
        }
        if (sender.hasPermission(PermissionNode.BOOK.getNode())) {
            sender.sendMessage("    book        " + (Object)ChatColor.YELLOW + "- Gives a book with enchantment descriptions");
        }
        if (sender.hasPermission(PermissionNode.ADMIN.getNode())) {
            sender.sendMessage("    reload      " + (Object)ChatColor.YELLOW + "- Reload the plugin");
            sender.sendMessage("    add          " + (Object)ChatColor.YELLOW + "- Add an enchantment to an item");
            sender.sendMessage("    stats <item> <level>      " + (Object)ChatColor.YELLOW + "- Displays enchantment stats");
            sender.sendMessage("    graph <item> <enchant> " + (Object)ChatColor.YELLOW + "- Displays a graph of stats");
        }
        return true;
    }

    @Override
    public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage((Object)ChatColor.YELLOW + this.plugin.getTag() + " Unknown command: " + (Object)ChatColor.WHITE + args[0]);
        return true;
    }

    private class HelpCommand
    implements ICommand {
        private HelpCommand() {
        }

        @Override
        public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
            return Commander.this.noArgs(sender, command, label);
        }
    }
}

