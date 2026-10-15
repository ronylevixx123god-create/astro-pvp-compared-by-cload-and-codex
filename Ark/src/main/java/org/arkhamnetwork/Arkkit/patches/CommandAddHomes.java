/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.PermissionAttachmentInfo
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

final class CommandAddHomes
        extends AbstractCommand {
    public CommandAddHomes(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Invalid Syntax. /addhomes <player name> <number>");
            return true;
        }
        String player_name = args[0];
        int homes_to_add = 0;
        try {
            homes_to_add = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(ChatColor.RED + "Invalid number specified, '" + homes_to_add + "'");
            return true;
        }
        if (player_name.contains("-") ? Bukkit.getPlayer(UUID.fromString(player_name)) == null : Bukkit.getPlayer(player_name) == null) {
            sender.sendMessage(ChatColor.RED + player_name + " is not online, cannot modify permissions!");
            return true;
        }
        int current_limit = EssentialsAddHomes.getEssentials().getSettings().getHomeLimit(EssentialsAddHomes.getEssentials().getUser(player_name.contains("-") ? UUID.fromString(player_name) : player_name));
        int new_limit = current_limit + homes_to_add;
        if (!EssentialsAddHomes.getEssentials().getConfig().getConfigurationSection("sethome-multiple").contains(EssentialsAddHomes.getEssentialsGroup(new_limit))) {
            Bukkit.getLogger().info("Creating new sethome-multiple group, " + EssentialsAddHomes.getEssentialsGroup(new_limit) + " ...");
            FileConfiguration fg = EssentialsAddHomes.getEssentials().getConfig();
            fg.getConfigurationSection("sethome-multiple").set(EssentialsAddHomes.getEssentialsGroup(new_limit), new_limit);
            try {
                fg.save("plugins/Essentials/config.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Player ply = player_name.contains("-") ? Bukkit.getPlayer(UUID.fromString(player_name)) : Bukkit.getPlayer(player_name);
        this.clearEssentialsHomeLimits(ply);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ply.getName() + " permission set essentials.sethome.multiple." + EssentialsAddHomes.getEssentialsGroup(new_limit) + " true");

        EssentialsAddHomes.getEssentials().reload();
        Bukkit.getLogger().info(ply.getName() + " now has a /sethome limit of " + new_limit + ", with permission essentials.sethome.multiple." + EssentialsAddHomes.getEssentialsGroup(new_limit));
        return true;
    }

    private void clearEssentialsHomeLimits(Player pl) {
        for (PermissionAttachmentInfo pio : pl.getEffectivePermissions()) {
            if (!pio.getPermission().startsWith("essentials.sethome.multiple.")) continue;
            Bukkit.getLogger().info("Removing permission " + pio.getPermission() + " from " + pl.getName() + "...");
            if (Bukkit.getPluginManager().isPluginEnabled("PermissionsEx") || Bukkit.getPluginManager().isPluginEnabled("CompilexPermissions")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + pl.getName() + " permission set " + pio.getPermission() + " false");
                continue;
            }
            pl.removeAttachment(pio.getAttachment());
        }
    }

    @Deprecated
    private String[] getAllPermissions(Player pl) {
        HashSet<String> permission_list = new HashSet<String>();
        for (PermissionAttachmentInfo pio : pl.getEffectivePermissions()) {
            permission_list.add(pio.getPermission());
        }
        return permission_list.toArray(new String[permission_list.size()]);
    }
}

