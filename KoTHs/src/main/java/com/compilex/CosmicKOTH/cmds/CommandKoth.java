/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.arkhamnetwork.Arkkit.patches.CombatLog
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.compilex.CosmicKOTH.cmds;

import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.utils.InventoryUtils;
import com.compilex.CosmicKOTH.utils.KothTopUtil;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

public class CommandKoth
implements CommandExecutor {
    public boolean onCommand(CommandSender se, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            Player player = (Player)se;
            if (CombatLog.inCombat((Player)player)) {
                player.sendMessage(CC.Red + "You cannot do this whilst in combat!");
                return true;
            }
            player.openInventory(InventoryUtils.getKothInventory());
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            Player player = (Player)se;
            player.sendMessage("");
            player.sendMessage(CC.translate("&e&lTop Koth Wins"));
            Map<String, Integer> kothTop = KothTopUtil.getKothTop();
            int counter = 0;
            for (Map.Entry<String, Integer> entry : kothTop.entrySet()) {
                String name = entry.getKey();
                int wins = entry.getValue();
                counter++;
                if (counter > 10) break;
                player.sendMessage(CC.translate("&e&l" + counter + ". &e" + name + " - &f" + wins + " Win(s)"));
            }
            player.sendMessage("");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("warp")) {
            if (CosmicKOTHAPI.isKOTHRunning()) {
                if (CosmicKOTHAPI.canTeleportToKOTH()) {
                    if (se instanceof Player) {
                        Player p = (Player)se;
                        if (CosmicKOTHPlugin.get_().getSpawn() != null) {
                            p.teleport(CosmicKOTHPlugin.get_().getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        } else {
                            p.sendMessage((Object)ChatColor.RED + "An internal server error has occurred while attempting to locate KOTH spawn.");
                        }
                    }
                } else {
                    se.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "The current KOTH event has already been capped.");
                    se.sendMessage((Object)ChatColor.GRAY + "You can no longer teleport in to this KOTH event.");
                }
            } else {
                se.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "No KOTH events are currently running.");
                se.sendMessage((Object)ChatColor.GRAY + "Use /koth to find the next scheduled KOTH.");
            }
        }
        return true;
    }
}

