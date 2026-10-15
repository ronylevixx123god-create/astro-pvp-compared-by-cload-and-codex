package com.astropvp.wildpvp.command;


import com.astropvp.wildpvp.AstroWildPvP;
import com.astropvp.wildpvp.queue.WildQueue;
import com.astropvp.wildpvp.util.KrizzUtil;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class WildPvPCommand implements CommandExecutor, Listener {
    private AstroWildPvP plugin;

    public WildPvPCommand(AstroWildPvP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            KrizzUtil.sendMessage(sender, "&c&l(!)&c You must be a &lPlayer&c.");
            return true;
        } else {
            Player player = (Player)sender;
            if (CombatLog.inCombat(player)) {
                player.sendMessage(CC.translate("&c&l(!)&c You cannot use /pvp while in combat."));
                return true;
            }
            if (args.length == 0) {
                this.plugin.getQueueManager().openQueueInventory(player);
                return true;
            } else if (args[0].equalsIgnoreCase("leave")) {
                WildQueue queue = this.plugin.getQueueManager().getQueueByHost(player);
                if (queue == null) {
                    KrizzUtil.sendMessage((LivingEntity)player, (String)"&c&l(!)&c You are not in the &lWild PvP Queue&c.");
                } else {
                    this.plugin.getQueueManager().removeExisitingQueue(queue);
                    KrizzUtil.sendMessage((LivingEntity)player, (String)"&c&l(!)&c You have left the &lWild PvP Queue&c.");
                }
                return true;
            } else {
                return true;
            }
        }
    }
}
