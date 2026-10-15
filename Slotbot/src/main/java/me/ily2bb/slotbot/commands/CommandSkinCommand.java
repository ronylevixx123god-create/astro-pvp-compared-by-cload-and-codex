package me.ily2bb.slotbot.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ily2bb.slotbot.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandSkinCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission("addskin.cmd")) {
            return false;
        }
        if (args.length == 1) {
            ItemStack item = ((Player) sender).getItemInHand();
            item = Utils.addSkin(args[0].toUpperCase(), item);
            ((Player) sender).setItemInHand(item);
            ((Player) sender).sendMessage(String.valueOf(CC.Red) + "Added " + args[0].toUpperCase() + " Cosmic NBT Data.");
            return false;
        }
        return false;
    }
}
