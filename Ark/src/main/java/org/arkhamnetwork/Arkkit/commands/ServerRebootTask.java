/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.commands;

import com.massivecraft.factions.util.TitleAPI;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

final class ServerRebootTask
        extends BukkitRunnable {
    private final String restartWarnMessage = ChatColor.translateAlternateColorCodes('&', "&e&l *** LOCAL SERVER REBOOTING IN &f&l[time]&e&l SECONDS ***");
    volatile int iteration = 60;

    public ServerRebootTask(boolean force) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "      *** An Astro reboot has been queued. *** ");
        Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "  This process occurs when Astro pulls a critical core update.");
        Bukkit.broadcastMessage("");

        if (force) {
            this.iteration = 0;
        }
    }

    public synchronized void run() {
        if (this.iteration < -10) {
            return;
        }
        switch (this.iteration) {
            case 60: {
                this.doBroadcast();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.closeInventory();
                }
                break;
            }
            case 45:
            case 30:
            case 15:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1: {
                this.doBroadcast();
                break;
            }
            case 10: {
                this.doBroadcast();
                Bukkit.getScheduler().runTask(Arkkit.get(), new Runnable() {

                    @Override
                    public void run() {
                        Bukkit.savePlayers();
                        Bukkit.getLogger().info("(CommandArkReboot) Saving all worlds...");
                        for (World world : Bukkit.getWorlds()) {
                            world.save();
                        }
                    }
                });
                break;
            }
            case -10: {
                Bukkit.getLogger().info("(CommandArkReboot) Stopping the server...");
                Bukkit.shutdown();
                this.cancel();
            }
        }
        --this.iteration;
    }

    private void doBroadcast() {
        Title title = new Title("", this.restartWarnMessage.replace("[time]", String.valueOf(this.iteration)), 5, 30 , 5);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title);
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(this.restartWarnMessage.replace("[time]", String.valueOf(this.iteration)));
        Bukkit.broadcastMessage("");
    }
}

