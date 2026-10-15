/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

final class CommandForceSpawn
        extends AbstractCommand {
    public CommandForceSpawn(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("arkkit.admin.forcespawn")) {
                return true;
            }
        } else {
            Bukkit.getLogger().info("This command cannot be used by console.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Usage " + ChatColor.WHITE + "/forcespawn <radius>");
            return true;
        }
        int radius = Integer.parseInt(args[0]);
        int x = 0;
        Player pcenter = (Player) sender;
        for (Entity ent : pcenter.getNearbyEntities(radius, radius, radius)) {
            Player p;
            if (!(ent instanceof Player) || (p = (Player) ent).isOp() || p.hasPermission("arkkit.admin.forcespawn"))
                continue;
            ++x;
            p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
        pcenter.sendMessage(ChatColor.GREEN + "You have sent " + x + " player(s) to " + Bukkit.getWorlds().get(0).getName() + "'s /spawn!");
        return true;
    }
}

