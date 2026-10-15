package org.arkhamnetwork.Arkkit.patches;

import com.gmail.nossr50.api.ExperienceAPI;
import java.util.ArrayList;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.patches.RedeemMCMMO;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandRedeem
        extends AbstractCommand {
    public CommandRedeem(String command, String usage, String description, ArrayList<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("redeem")) {
            if (!sender.hasPermission("redeemMCMMO.redeem")) {
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
                return true;
            }
            Player player = (Player)sender;
            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "/redeem <skill> <levels>");
                player.sendMessage(ChatColor.RED + "mcMMO Credits: " + ChatColor.GRAY + RedeemMCMMO.getConfig().getInt(String.valueOf(player.getUniqueId().toString()) + ".credits"));
                player.sendMessage(ChatColor.GRAY + "Skills: taming, swords, alchemy, unarmed, archery, axes, acrobatics, fishing, excavation, mining, herbalism, repair, woodcutting");
            } else if (args.length == 2) {
                String skillType = args[0];
                int cap = 0;
                if (!(skillType.equalsIgnoreCase("taming") || skillType.equalsIgnoreCase("swords") || skillType.equalsIgnoreCase("alchemy") || skillType.equalsIgnoreCase("unarmed") || skillType.equalsIgnoreCase("archery") || skillType.equalsIgnoreCase("axes") || skillType.equalsIgnoreCase("acrobatics") || skillType.equalsIgnoreCase("fishing") || skillType.equalsIgnoreCase("excavation") || skillType.equalsIgnoreCase("mining") || skillType.equalsIgnoreCase("herbalism") || skillType.equalsIgnoreCase("repair") || skillType.equalsIgnoreCase("woodcutting"))) {
                    player.sendMessage(ChatColor.RED + skillType + " is not a valid skill!");
                    return true;
                }
                cap = ExperienceAPI.getLevelCap((String)skillType);
                try {
                    Integer.parseInt(args[1]);
                }
                catch (NumberFormatException ex) {
                    player.sendMessage(ChatColor.RED + "Please specify a valid numeric <levels> value.");
                    return true;
                }
                int amount = Integer.parseInt(args[1]);
                int oldamount = RedeemMCMMO.getConfig().getInt(String.valueOf(player.getUniqueId().toString()) + ".credits");
                if (oldamount < amount) {
                    player.sendMessage(ChatColor.RED + "You do not have enough /credits!");
                    player.sendMessage(ChatColor.GRAY + "MCMMO Credits: " + oldamount);
                    return true;
                }
                if (amount <= 0) {
                    player.sendMessage(ChatColor.RED + "You must specify a positive <levels> value.");
                    return true;
                }
                int skillLevel = ExperienceAPI.getLevel((Player)player, (String)skillType);
                if (skillLevel + amount > cap) {
                    if (skillLevel >= cap) {
                        player.sendMessage(ChatColor.RED + "You already have the MAX level of " + skillType + "! (" + skillLevel + ")");
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot add more than " + (cap - skillLevel) + " more levels to " + skillType + "!");
                    }
                    return true;
                }
                int newamount = oldamount - amount;
                RedeemMCMMO.getConfig().set(String.valueOf(player.getUniqueId().toString()) + ".credits", (Object)newamount);
                ExperienceAPI.addLevel((Player)player, (String)skillType, (int)amount);
                player.sendMessage(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD.toString() + " -" + amount + " MCMMO CREDITS");
                player.sendMessage(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.BOLD.toString() + " +" + amount + " " + skillType.toUpperCase() + " LEVELS");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                return true;
            }
        }
        return true;
    }
}
