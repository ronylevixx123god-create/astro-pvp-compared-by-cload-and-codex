package me.ifamasssxd.cosmicbosses.commands;

import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class CommandSpawnBoss implements CommandExecutor {
    HashSet<Byte> empty;

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage: /spawnboss <bossName>");
            return true;
        }
        final Player p = (Player) sender;
        final String bossName = args[0];
        final BossType type = BossType.getBossType(bossName);
        if (type == null) {
            p.sendMessage(ChatColor.RED + "Invalid Boss type given: " + bossName);
            return true;
        }
        p.performCommand("boss spawn " + type.name());
        return false;
    }
}
