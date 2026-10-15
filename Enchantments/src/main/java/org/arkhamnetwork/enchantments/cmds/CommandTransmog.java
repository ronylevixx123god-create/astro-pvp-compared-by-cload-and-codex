package org.arkhamnetwork.enchantments.cmds;

import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTransmog implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        final String p_name = args[0];
        if (Bukkit.getPlayer(p_name) != null) {
            final Player pl = Bukkit.getPlayer(p_name);
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "+ 1x Transmog Scroll");
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(EnchantmentBook.getTransmogScroll());
            } else {
                pl.getWorld().dropItem(pl.getLocation(), EnchantmentBook.getTransmogScroll());
            }
        }
        return true;
    }
}
