package me.astropvp.astrospawners.commands;

import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCheckHashMap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) return false;
        commandSender.sendMessage(ChatColor.GREEN + "The Mobs Hashmap size is: " + AstroSpawners.getInstance().getMobStackerUtils().getMobMap().size());
        return true;
    }
}
