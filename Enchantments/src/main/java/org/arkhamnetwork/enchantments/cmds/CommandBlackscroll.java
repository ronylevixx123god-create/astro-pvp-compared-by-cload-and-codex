package org.arkhamnetwork.enchantments.cmds;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBlackscroll implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length >= 2) {
            Player pl = Bukkit.getPlayer(args[1]);
            if (pl == null) {
                sender.sendMessage(CC.Red + "Player not found!");
                return true;
            }
            Integer giveNum = (args.length >= 3) ? NumberUtils.parseIntOrNull(args[2]) : null;
            Integer maxNum = (args.length >= 4) ? NumberUtils.parseIntOrNull(args[3]) : null;
            if (args[0].equalsIgnoreCase("heroic")) {
                ItemStack item;
                if (giveNum != null) {
                    if (maxNum != null) {
                        item = EnchantmentBook.getHeroicBlackscroll(giveNum, maxNum, true);
                    } else {
                        item = EnchantmentBook.getHeroicBlackscroll(10, giveNum, true);
                    }
                } else {
                    item = EnchantmentBook.getHeroicBlackscroll(true);
                }
                PlayerUtil.giveOrDropProtectedItem(pl, item, -1);
                sender.sendMessage(CC.Red + "Heroic blackscroll given to " + pl.getName());
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                PlayerUtil.giveOrDropProtectedItem(pl, (giveNum != null) ? EnchantmentBook.getBlackScroll(giveNum) : EnchantmentBook.getBlackScroll(), -1);
                sender.sendMessage(CC.Red + "Blackscroll given to " + pl.getName() + "!");
                return true;
            }

            if (args[0].equalsIgnoreCase("mastery")) {
                PlayerUtil.giveOrDropProtectedItem(pl, EnchantmentBook.getMasteryBlackscroll(100, 100), -1);
                sender.sendMessage(CC.Red + "Mastery Blackscroll given to " + pl.getName() + "!");
                return true;
            }
        }
        sender.sendMessage(CC.Red + "Usage: /blackscroll heroic/mastery/give <playerName> [percent/maxPercent]");
        return false;
    }
}
