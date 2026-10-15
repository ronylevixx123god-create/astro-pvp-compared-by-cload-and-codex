package org.arkhamnetwork.Arkkit.patches;

import java.util.ArrayList;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.patches.RedeemMCMMO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandCredits
        extends AbstractCommand {
    public CommandCredits(String command, String usage, String description, ArrayList<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("credits")) {
            if (!sender.hasPermission("redeemMCMMO.credits")) {
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
                return true;
            }
            Player player = (Player)sender;
            if (args.length == 0) {
                String id = player.getUniqueId().toString();
                String playerId = RedeemMCMMO.getConfig().getString(id);
                if (playerId != null) {
                    int credits = RedeemMCMMO.getConfig().getInt(String.valueOf(id) + ".credits");
                    player.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "MCMMO Credits: " + ChatColor.UNDERLINE + credits);
                    player.sendMessage(ChatColor.GRAY + "Use /redeem to allocate these credits to your mcMMO stats.");
                    return true;
                }
                player.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "MCMMO Credits: " + ChatColor.UNDERLINE + 0);
                player.sendMessage(ChatColor.GRAY + "Purchase mcMMO credits at " + ChatColor.UNDERLINE + Arkkit.get().getConfig().getString("language.spigot_branding.store_url"));
                return true;
            }
            if (args.length == 1) {
                if (!sender.hasPermission("redeemMCMMO.credits.others")) {
                    return true;
                }
                Player target = Bukkit.getServer().getPlayer(args[0]);
                String targetId = target.getUniqueId().toString();
                if (targetId != null) {
                    int credits = RedeemMCMMO.getConfig().getInt(String.valueOf(targetId) + ".credits");
                    player.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + target.getName() + "'s MCMMO Credits: " + ChatColor.UNDERLINE + credits);
                    return true;
                }
                player.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + target.getName() + "'s MCMMO Credits: " + ChatColor.UNDERLINE + 0);
                return true;
            }
        }
        return true;
    }
}
