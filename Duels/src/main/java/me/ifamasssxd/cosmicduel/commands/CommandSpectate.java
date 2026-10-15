/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicduel.commands;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.listeners.SpectateInventory;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpectate
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if (PlayerUtils.inCombat(p)) {
            p.sendMessage(ChatColor.RED + "You cannot spectate while in combat.");
            return true;
        }
        if (DuelAPI.isSpectator(p)) {
            p.sendMessage(ChatColor.RED + "You are already spectating a duel.");
            p.sendMessage(ChatColor.GRAY + "Use /duel leave to leave the current duel.");
            return true;
        }
        if (DuelAPI.canDuel(p)) {
            p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.9f);
            SpectateInventory.openInventory(p);
        } else {
            p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot spectate any matches at this time.");
            if (DuelAPI.hasDuel(p)) {
                NewDuel duel = DuelAPI.getDuel(p);
                if (DuelAPI.isDuelQueued(duel)) {
                    p.sendMessage(ChatColor.RED + "You are currently #" + DuelAPI.getDuelPosition(duel) + " in queue for the " + duel.getArena().getName() + " Arena" + ChatColor.RED + ".");
                } else {
                    p.sendMessage(ChatColor.GRAY + "You are currently in a duel.");
                }
            }
        }
        return false;
    }
}

