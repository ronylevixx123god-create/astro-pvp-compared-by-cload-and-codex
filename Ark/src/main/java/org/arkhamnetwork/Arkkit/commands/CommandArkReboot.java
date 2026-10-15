/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.data.SpawnerDataHelper;
import me.astropvp.astrospawners.utils.PlayerData;
import me.astropvp.astrospawners.utils.PlayerDataManager;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.StaticMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArkReboot
        implements CommandExecutor {
    public static boolean restart_task_exists = false;
    private PlayerDataManager manager;
    private ServerRebootTask serverRebootTask;

    public CommandArkReboot() {
        Bukkit.getPluginManager().registerEvents(new ArkRebootListener(), Arkkit.get());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("arkkit.command.admin")) {
            sender.sendMessage(StaticMessages.NO_PERMISSION);
            return true;
        }

        if (restart_task_exists) {
            sender.sendMessage(ChatColor.YELLOW + "There is already a pending restart task running.");
            return true;
        }

        manager = new PlayerDataManager(AstroSpawners.getInstance());
        restart_task_exists = true;
        this.serverRebootTask = new ServerRebootTask(false);
        this.serverRebootTask.runTaskTimer(Arkkit.get(), 20L, 20L);

        return true;
    }
}

