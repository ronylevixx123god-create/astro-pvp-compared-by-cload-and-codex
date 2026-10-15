package me.astropvp.astropvpskins.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMultiSkin implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (!player.isOp())
            return true;
        if (args.length != 3) {
            player.sendMessage(CC.Red + "Correct Usage: /multiskin <player> <skin1> <skin2>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        SkinType skin1 = SkinType.valueOf(args[1]);
        SkinType skin2 = SkinType.valueOf(args[2]);
        if (skin1.getItemSkinType() != skin2.getItemSkinType()) {
            player.sendMessage(CC.Red + "Both skins must be of the same type.");
            return true;
        }
        target.getInventory().addItem(skin1.getMultiSkin(skin2));
        return false;
    }
}
