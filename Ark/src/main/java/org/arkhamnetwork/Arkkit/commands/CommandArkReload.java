package org.arkhamnetwork.Arkkit.commands;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.StaticMessages;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandArkReload implements CommandExecutor {
    private Arkkit plugin = null;

    public CommandArkReload(Plugin p) {
        this.plugin = (Arkkit) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("arkkit.command.admin")) {
            sender.sendMessage(StaticMessages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /arkreload <patch or *>");
            sender.sendMessage("Loaded Patches: [" + ArkPlugins.getLoadedPatches() + "]");
            return true;
        }
        String patch = args[0];
        if (patch.equals("*")) {
            this.plugin.reloadConfig();
            for (String s : this.plugin.getConfig().getKeys(true)) {
                if (!s.startsWith("patches.")) continue;
                Bukkit.getLogger().info(s + ": " + this.plugin.getConfig().getBoolean(s));
            }
            sender.sendMessage("Configuration reloaded.");
            sender.sendMessage("Killing patches...");
            this.plugin.unregisterPatches();
            sender.sendMessage("Injecting patches...");
            this.plugin.registerPatches();
        } else {
            sender.sendMessage("Killing patch...");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "arkunregister " + patch);
            sender.sendMessage("Injecting patch...");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "arkregister " + patch);
            sender.sendMessage("Done!");
        }
        return true;
    }
}

