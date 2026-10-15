package org.arkhamnetwork.enchantments.cmds;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import org.arkhamnetwork.enchantments.RandomizationScroll;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandEnchScroll implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("/enchscroll <type> <player> (rarity)");
            sender.sendMessage("i.e.) /enchorb randomization Vaquxine LEGENDARY");
            return true;
        }
        final String p_name = args[1];
        if (Bukkit.getPlayer(p_name) != null) {
            final Player pl = Bukkit.getPlayer(p_name);
            ItemStack i = null;
            if (args[0].equalsIgnoreCase("randomization")) {
                i = RandomizationScroll.getRandomizationScroll(ItemRarity.valueOf(args[2]));
            }
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(i);
            } else {
                pl.getWorld().dropItem(pl.getLocation(), i);
            }
        }
        return true;
    }
}
