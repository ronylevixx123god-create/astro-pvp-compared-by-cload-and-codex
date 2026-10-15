package me.astropvp.astrospawners.commands;

import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.utils.MobStackerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandClearHashMap implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) return false;

        Bukkit.getWorld("world").getEntities().forEach(entity -> {
            if (AstroSpawners.getInstance().getMobStackerUtils().isStackedMob(entity)) {
                AstroSpawners.getInstance().getMobStackerUtils().removeStackedMob(entity);
            }
        });
        AstroSpawners.getInstance().getMobStackerUtils().getMobMap().clear();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Cleared all stacked mobs and Hashmap");
     return true;
    }
}
