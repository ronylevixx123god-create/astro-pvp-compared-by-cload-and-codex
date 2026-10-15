package net.astropvp.armorsets.commands;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class CommandGiveHeroicUpgrade
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No permission.");
            return true;
        }
        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Double percent;
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Invalid player given!");
                    return true;
                }
                ArmorSetType set = AstroArmorSetsAPI.armorSetTypeFromName(args[2]);
                if (set == null) {
                    sender.sendMessage(CC.Red + "Invalid armor set: " + Arrays.toString(ArmorSetType.values()));
                    return true;
                }

                percent = args.length == 4 ? NumberUtils.parseDoubleOrNull(args[3]) : null;


                PlayerUtil.giveOrDropProtectedItem(player, AstroArmorSetsAPI.createArmorSetUpgrade(set, percent), -1);
                sender.sendMessage(CC.Red + "Added " + set.name() + " ArmorSet at " + percent + "% to " + player.getName() + "'s Inventory");
                return true;
            }
        }
        sender.sendMessage(CC.Red + "/heroicupgrade give <player> <armorset> [percent]");
        return false;
    }
}

