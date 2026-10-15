package org.arkhamnetwork.enchantments.cmds;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.menus.EnchanterGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEnchanter implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player p = (Player) sender;
        new EnchanterGUI(p).openGUI(Enchantments.getInstance());
        p.playSound(p.getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.4f);
        return true;
    }
}
