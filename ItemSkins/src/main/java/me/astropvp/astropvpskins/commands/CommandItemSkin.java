package me.astropvp.astropvpskins.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.inventories.ItemSkinsInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandItemSkin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(args.length == 0 || !player.isOp()) {
            ItemSkinsInventory.openInventory(player); //TODO: <-----
            player.sendMessage(CC.translate("&a&l(!)&a Opening the Skins Inventory..."));
        } else {
            if(args[0].equalsIgnoreCase("give") && args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                SkinType type = SkinType.valueOf(args[2]);
                if(type.getItemSkin() == null) {
                    player.sendMessage(CC.Red + "Correct Usage: itemskin give <player> <skin>");
                    player.sendMessage(CC.Red + "Skin List: " + Arrays.toString(SkinType.values()));
                    return true;
                }
                target.getInventory().addItem(type.getItem());
            } else {
                player.sendMessage(CC.Red + "Correct Usage: itemskin give <player> <skin>");
                player.sendMessage(CC.Red + "Skin List: " + Arrays.toString(SkinType.values()));
            }
        }
        return false;
    }
}
