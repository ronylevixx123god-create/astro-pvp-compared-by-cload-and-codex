package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveColoredLoreTag extends AbstractCommand {
    public CommandGiveColoredLoreTag(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/givecoloredloretag <name>");
            return true;
        }
        String pname = args[0];
        Player pl = Bukkit.getPlayer(pname);
        if (pl == null) {
            sender.sendMessage(ChatColor.RED + "Could not locate player " + pname + "!");
            return true;
        }
        if (pl.getInventory().firstEmpty() != -1) {
            pl.getInventory().addItem(ColoredLoreTag.getColoredLoreTagItem());
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "You have received an Colored Item Lore Crystal!");
        } else {
            pl.getWorld().dropItem(pl.getLocation(), ColoredLoreTag.getColoredLoreTagItem());
            pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, a lore crystal has been dropped at your feet.");
        }
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Gave 1x Colored Lore Crystal to " + pl.getName() + "!");
        return true;
    }
}

