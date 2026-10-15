/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.vaqxine.NetworkManager.NetworkManager
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

final class CommandLogout
        extends AbstractCommand {
    public CommandLogout(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    protected static int getCombatSecondsLeft(Player pl) {
        if (pl.hasMetadata("cl_combat") && pl.getMetadata("cl_combat").get(0) != null) {
            return (int) (CombatLog.getCombatTagDuration(pl) - (System.currentTimeMillis() - pl.getMetadata("cl_combat").get(0).asLong())) / 1000;
        }
        return 0;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && sender.isOp()) {
            if (CombatLogLogoutListener.spawnNPC) {
                CombatLogLogoutListener.spawnNPC = false;
                Arkkit.get().getConfig().set("settings.combat_log.spawnNPC", false);
                Arkkit.get().saveConfig();
            } else if (!CombatLogLogoutListener.spawnNPC) {
                CombatLogLogoutListener.spawnNPC = true;
                Arkkit.get().getConfig().set("settings.combat_log.spawnNPC", true);
                Arkkit.get().saveConfig();
            }
            sender.sendMessage("combatLog.spawnNPC => " + CombatLogLogoutListener.spawnNPC);
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!(Arkkit.get().shutting_down || !CombatLog.inCombat(p) || Bukkit.getPluginManager().getPlugin("NetworkManager") != null && CombatLog.isPvPDisabled(p.getLocation()))) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) You cannot logout until you are out of combat.");
                p.sendMessage(ChatColor.GRAY + "You will be out of combat in " + ChatColor.RED + CommandLogout.getCombatSecondsLeft(p) + " second(s)");
                return true;
            }
            for (int x = 0; x < 20; ++x) {
                p.sendMessage("");
            }
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "LOGGING OUT...");
            p.removeMetadata("cl_combat", Arkkit.get());
        }
        return true;
    }
}

