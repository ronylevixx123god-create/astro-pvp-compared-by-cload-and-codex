package org.arkhamnetwork.enchantments.cmds;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Random;

public class CommandEnchBook implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length < 1) {
            this.sendHelp(sender);
            return false;
        }
        final String p_name = args[0];
        if (args.length < 2) {
            if (Bukkit.getPlayer(p_name) != null) {
                final Player pl = Bukkit.getPlayer(p_name);
                for (final CustomEnchantment ce : EnchantmentAPI.getEnchantments()) {
                    if (Enchantment.getByName(ce.name()) != null) {
                        continue;
                    }
                    if (pl.getInventory().firstEmpty() != -1) {
                        pl.getInventory().addItem(EnchantmentBook.getEnchantmentBook(ce, ce.getMaxLevel()));
                    } else {
                        pl.getWorld().dropItem(pl.getLocation(), EnchantmentBook.getEnchantmentBook(ce, ce.getMaxLevel()));
                    }
                }
            }
            return true;
        }
        final Player pl = Bukkit.getPlayer(p_name);
        final CustomEnchantment ce2 = EnchantmentAPI.getEnchantment(args[1].replace("_", " "));
        if (ce2 == null) {
            pl.sendMessage(ChatColor.RED + "Invalid enchantment given!");
            return true;
        }
        int amount = 0;
        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
                /*
                if (amount > ce2.getMaxLevel()) {
                    sender.sendMessage(ChatColor.RED + "Max level: " + ce2.getMaxLevel());
                    return true;
                }

                 */
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage("Please enter a valid level!");
            }
        }
        if (amount == 0) {
            amount = new Random().nextInt(ce2.getMaxLevel()) + 1;
        }
        if (pl.getInventory().firstEmpty() != -1) {
            pl.getInventory().addItem(EnchantmentBook.getEnchantmentBook(ce2, amount));
        } else {
            pl.getWorld().dropItem(pl.getLocation(), EnchantmentBook.getEnchantmentBook(ce2, amount));
        }
        return true;
    }

    private void sendHelp(final CommandSender sender) {
        sender.sendMessage(CC.Red + "/enchbook <player> [enchant] [level] ");
    }
}
