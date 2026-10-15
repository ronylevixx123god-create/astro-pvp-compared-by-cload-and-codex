package me.astropvp.cratekeys.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveCrateKey implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!sender.isOp())
            return false;
        if (args.length >= 3) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(String.valueOf(CC.RedB) + "(!) " + CC.Red + "That player is not online!");
                return false;
            }
            CrateType type = CrateType.valueOf(args[1].toUpperCase());
            int amount = Integer.parseInt(args[2]);
            sender.sendMessage(String.valueOf(CC.Green) + "You gave " + player.getName() + " " + amount + "x Crate Key(s) of type: " +
                    type.getName() + "!");

            AstroCrateKeysAPI.givePlayerKeys(player, type, amount);

            return false;
        }
        sender.sendMessage(String.valueOf(CC.Yellow) + "Usage: " + CC.White + "/givecratekey [USER] [CRATE TYPE] [AMOUNT]");
        return false;
    }
}
