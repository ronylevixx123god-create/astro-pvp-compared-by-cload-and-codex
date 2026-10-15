package me.astropvp.astrospawners.commands;

import java.util.Iterator;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.customitems.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CommandGiveSpawner implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!sender.isOp()) {
            return false;
        }
        if (args.length <= 1) {
            Iterator<String> iterator = CustomItem.getCustomItems().keySet().iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                String x = iterator.next();
                builder.append(CC.Red + x + (iterator.hasNext() ? ", " : ""));
            }
            sender.sendMessage(builder.toString());
            sender.sendMessage("");
            sender.sendMessage(CC.Red + "/givespawner <custom usage> <player> ...");
            return false;
        }
        if (!CustomItem.getCustomItems().containsKey(args[0])) {
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = CustomItem.getCustomItems().keySet().iterator();
            while (iterator.hasNext()) {
                String x = iterator.next();
                builder.append(CC.Red + x + (iterator.hasNext() ? "," : ""));
            }
            sender.sendMessage(builder.toString());
            sender.sendMessage("");
            sender.sendMessage(CC.Red + "Invalid Usage: " + args[0]);
            sender.sendMessage(CC.Red + "/givespawner <custom usage> <player> ...");
            return false;
        }
        if (!Bukkit.getPlayer(args[1]).isOnline()) {
            sender.sendMessage(CC.Red + "Player is not online!");
            return false;
        }
        String[] result = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            result[i - 1] = args[i];
        }
        CustomItem.getCustomItems().get(args[0]).proccessCommand(sender, result);
        return false;
    }

}
