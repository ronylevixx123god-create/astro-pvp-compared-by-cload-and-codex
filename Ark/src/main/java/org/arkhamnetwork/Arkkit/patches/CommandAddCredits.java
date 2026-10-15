package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.patches.RedeemMCMMO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandAddCredits
        extends AbstractCommand {
    public CommandAddCredits(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("addcredits")) {
            if (!sender.hasPermission("redeemMCMMO.addcredits")) {
                return true;
            }
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.RED + "Too little arguments!");
            } else {
                if (args.length == 2) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " is not online or doesn't exist!");
                        return true;
                    }
                    String targetPlayer = target.getUniqueId().toString();
                    String targetId = RedeemMCMMO.getConfig().getString(targetPlayer);
                    try {
                        Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "The amount of credits must be a whole number!");
                        return true;
                    }
                    int amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        sender.sendMessage(ChatColor.RED + "The amount must be a positive number!");
                        return true;
                    }
                    if (targetId != null) {
                        int oldAmount = RedeemMCMMO.getConfig().getInt(String.valueOf(targetPlayer) + ".credits");
                        int newAmount = oldAmount + amount;
                        RedeemMCMMO.getConfig().set(String.valueOf(targetPlayer) + ".credits", (Object)newAmount);
                        sender.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.GOLD + args[0] + ", " + amount + ChatColor.YELLOW + " MCMMO credits.");
                        target.sendMessage(ChatColor.YELLOW  + "(!) " + ChatColor.YELLOW + "You have just received " + ChatColor.UNDERLINE + amount + ChatColor.YELLOW + " MCMMO credits.");
                        target.sendMessage(ChatColor.GRAY + "mcMMO Credit Balance: " + ChatColor.YELLOW + newAmount);
                        target.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/credits" + ChatColor.GRAY + " for help with redeeming them!");
                        return true;
                    }
                    RedeemMCMMO.getConfig().set(String.valueOf(targetPlayer) + ".credits", (Object)amount);
                    sender.sendMessage(ChatColor.GREEN + "You have given " + ChatColor.GOLD + args[0] + ", " + amount + ChatColor.GREEN + " MCMMO credits.");
                    sender.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.GOLD + args[0] + ", " + amount + ChatColor.YELLOW + " MCMMO credits.");
                    target.sendMessage(ChatColor.YELLOW  + "(!) " + ChatColor.YELLOW + "You have just received " + ChatColor.UNDERLINE + amount + ChatColor.YELLOW + " MCMMO credits.");
                    target.sendMessage(ChatColor.GRAY + "mcMMO Credit Balance: " + ChatColor.YELLOW + amount);
                    target.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/credits" + ChatColor.GRAY + " for help with redeeming them!");
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
            }
        }
        return true;
    }
}
