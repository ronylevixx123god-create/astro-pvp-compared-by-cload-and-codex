package com.cosmicpvp.cosmicwildpvp.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.concurrent.TimeUnit;

import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.queue.SingleWildQueue;
import com.cosmicpvp.cosmicwildpvp.util.BryUtil;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;

public class WildPvPCommand implements CommandExecutor, Listener
{
    private WildPvPPlugin plugin;

    public WildPvPCommand(final WildPvPPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            BryUtil.sendMessage(sender, "&c&l(!)&c You must be a &lPlayer&c.");
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            if (((Player)sender).hasMetadata("cl_combat")) {
                final long time = ((Player)sender).getMetadata("cl_combat").get(0).asLong();
                final long timeSince = System.currentTimeMillis() - time;
                if (timeSince < TimeUnit.SECONDS.toMillis(22L)) {
                    sender.sendMessage(CC.translate("&c&l(!)&c You must be out of combat for at least 7 seconds before entering the Wild PvP Queue!"));
                    return true;
                }
            }
            this.plugin.getQueueManager().openQueueInventory(player);
            return true;
        }
        if (!args[0].equalsIgnoreCase("leave")) {
            return true;
        }
        final SingleWildQueue queue = this.plugin.getQueueManager().getQueueByHost(player);
        if (queue == null) {
            BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You are not in the &lWild PvP Queue&c.");
            return true;
        }
        this.plugin.getQueueManager().removeExisitingQueue(queue);
        BryUtil.sendMessage((CommandSender)player, "&c&l(!)&c You have left the &lWild PvP Queue&c.");
        return true;
    }
}
