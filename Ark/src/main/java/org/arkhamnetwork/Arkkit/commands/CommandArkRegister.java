/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.commands;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.StaticMessages;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandArkRegister
        implements CommandExecutor {
    private Arkkit plugin = null;

    public CommandArkRegister(Plugin p) {
        this.plugin = (Arkkit) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("arkkit.command.admin")) {
            sender.sendMessage(StaticMessages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /arkregister <patch>");
            sender.sendMessage("Unloaded Patches: [" + ArkPlugins.getUnloadedPatches() + "]");
            return true;
        }
        String patch = args[0];
        try {
            if (this.plugin.registerPatch(ArkPlugins.valueOf(patch.toUpperCase()).getArkPlugin())) {
                sender.sendMessage("Loaded patches." + patch.toLowerCase());
                this.plugin.getConfig().set("patches." + patch.toLowerCase(), true);
                return true;
            }
            sender.sendMessage("Patch '" + patch + "' is already loaded!");
            return true;
        } catch (IllegalArgumentException err) {
            sender.sendMessage("No valid '" + patch + "' patch!");
            return true;
        }
    }
}

