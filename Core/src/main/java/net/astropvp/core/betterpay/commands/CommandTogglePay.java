/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.betterpay.commands;

import net.astropvp.core.AstroCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTogglePay
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        boolean toggle = AstroCore.getInstance().getPayToggles().togglePay(p);
        ChatColor color = toggle ? ChatColor.RED : ChatColor.GREEN;
        p.sendMessage((Object)color + ChatColor.BOLD.toString() + "(!) " + (Object)color + "You toggled /pay " + (toggle ? (Object)color + ChatColor.BOLD.toString() + "OFF" : (Object)color + ChatColor.BOLD.toString() + "ON"));
        p.sendMessage((Object)ChatColor.GRAY + "Players will " + (toggle ? "no longer" : "now") + " be able to send you money with /pay.");
        return false;
    }
}

