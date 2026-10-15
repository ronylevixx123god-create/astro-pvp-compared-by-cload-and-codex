package me.astropvp.cratekeys.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CratePlacementItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGiveCrate implements CommandExecutor {
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
            Material material = Material.valueOf(args[2].toUpperCase());
            sender.sendMessage(CC.Green + "You gave " + player.getName() + " " + 1 + "x Crate(s) of type: " +
                    type.getName() + "!");
            ItemStack item = CratePlacementItem.getItem(type, material);
            player.getInventory().addItem(item);
            return false;
        }
        sender.sendMessage("");
        sender.sendMessage(String.valueOf(CC.RedB) + CC.Underline + "Give Crate Command Arguments");
        sender.sendMessage(String.valueOf(CC.Yellow) + "Usage: " + CC.White + "/givecrate [PLAYER] [CRATE] [MATERIAL]");
        sender.sendMessage("");
        return false;
    }
}
