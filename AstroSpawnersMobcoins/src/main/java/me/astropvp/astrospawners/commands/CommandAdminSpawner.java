package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandAdminSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No Perms.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(CC.Red + "Wrong usage.");
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            AstroSpawners.getInstance().getConfigManager().reloadFiles();
            sender.sendMessage(CC.translate("&aReloaded config..."));
            return false;
        }
        return false;
    }
}
