/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.commands;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.StaticMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommandArkConfig
        implements CommandExecutor {
    private Arkkit plugin = null;

    public CommandArkConfig(Plugin p) {
        this.plugin = (Arkkit) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("arkkit.command.admin")) {
            sender.sendMessage(StaticMessages.NO_PERMISSION);
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /arkconfig <set/add/remove/get> <key> (value)");
            if (args.length == 0) {
                for (String s : this.plugin.getConfig().getKeys(true)) {
                    if (!s.contains(".")) continue;
                    sender.sendMessage(s + ": " + this.plugin.getConfig().get(s));
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                this.plugin.reloadConfig();
                sender.sendMessage(ChatColor.RED + "Reloaded config!");
            }
            return true;
        }
        String opperation = args[0];
        String key = args[1];
        String val = "";
        for (int i = 2; i < args.length; ++i) {
            val = val + args[i];
            if (i + 1 >= args.length) continue;
            val = val + " ";
        }
        if (opperation.equalsIgnoreCase("recopy")) {
            this.plugin.getConfig().options().copyDefaults(true);
            this.plugin.saveConfig();
            return true;
        }
        if (opperation.equalsIgnoreCase("get")) {
            for (String s : this.plugin.getConfig().getKeys(true)) {
                if (!s.equals(key)) continue;
                sender.sendMessage(s + ": " + this.plugin.getConfig().get(s));
            }
            return true;
        }
        if (opperation.equalsIgnoreCase("set")) {
            if (val.equals("true")) {
                this.plugin.getConfig().set(key, Boolean.TRUE);
                this.plugin.saveConfig();
            } else if (val.equals("false")) {
                this.plugin.getConfig().set(key, Boolean.FALSE);
                this.plugin.saveConfig();
            } else {
                try {
                    double i = Double.parseDouble(val);
                    this.plugin.getConfig().set(key, i);
                    this.plugin.saveConfig();
                } catch (NumberFormatException nfe) {
                    this.plugin.getConfig().set(key, val);
                    this.plugin.saveConfig();
                }
            }
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[Compilex]" + ChatColor.WHITE.toString() + " Set " + key + " => " + val);
        } else if (opperation.equalsIgnoreCase("add")) {
            List list = this.plugin.getConfig().getList(key);
            try {
                int i = Integer.parseInt(val);
                list.add(i);
            } catch (NumberFormatException nfe) {
                list.add(val);
            }
            this.plugin.getConfig().set(key, list);
            this.plugin.saveConfig();
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[Compilex]" + ChatColor.WHITE.toString() + " Added " + key + " TO " + val);
        } else if (opperation.equalsIgnoreCase("remove")) {
            List list = this.plugin.getConfig().getList(key);
            try {
                int i = Integer.parseInt(val);
                list.remove(i);
            } catch (NumberFormatException nfe) {
                list.remove(val);
            }
            this.plugin.getConfig().set(key, list);
            this.plugin.saveConfig();
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[Compilex]" + ChatColor.WHITE.toString() + " Removed " + key + " FROM " + val);
        }
        return true;
    }
}

