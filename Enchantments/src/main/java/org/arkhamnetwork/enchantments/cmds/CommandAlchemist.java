package org.arkhamnetwork.enchantments.cmds;

import org.arkhamnetwork.enchantments.CosmicAlchemist;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAlchemist implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player p = (Player) sender;
        p.openInventory(CosmicAlchemist.getExchangeInventory());
        p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0f, 0.8f);
        return true;
    }
}
