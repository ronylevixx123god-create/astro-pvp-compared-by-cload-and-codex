package me.ifamasssxd.cosmicconquest.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import me.ifamasssxd.cosmicconquest.CosmicConquests;
import org.bukkit.inventory.ItemStack;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandFToken implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 3) {
            final boolean giveHeroic = args[0].equalsIgnoreCase("giveh") || args[0].equalsIgnoreCase("giveheroic");
            if (args[0].equalsIgnoreCase("give") || giveHeroic) {
                final Player player = Bukkit.getPlayer(args[1]);
                if (!StringUtils.isNumeric(args[2])) {
                    sender.sendMessage(ChatColor.RED + "Please enter a valid number!");
                    return true;
                }
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player!");
                    return true;
                }
                final int val = Integer.parseInt(args[2]);
                final ItemStack items = giveHeroic ? CosmicConquestAPI.createHeroicToken(val) : CosmicConquestAPI.createFactionToken(val);
                player.getInventory().addItem(items);
                sender.sendMessage(ChatColor.RED + "Given " + val + "x " + (giveHeroic ? "heroic" : "") + " faction tokens to " + player.getName());
                return true;
            }
            else if (args[0].equalsIgnoreCase("setmax")) {
                final int min = Integer.parseInt(args[1]);
                final int max = Integer.parseInt(args[2]);
                CosmicConquests.get().getManager().setMinMaxCrystals(min, max);
                sender.sendMessage(ChatColor.RED + "Min / max set to " + min + " - " + max);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "/ftoken give <player> <amount>");
        return false;
    }
}
