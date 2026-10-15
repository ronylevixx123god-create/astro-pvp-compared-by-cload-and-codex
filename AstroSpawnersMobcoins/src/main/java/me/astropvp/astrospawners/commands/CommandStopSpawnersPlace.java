package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStopSpawnersPlace implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.isOp()) return false;

        boolean allowSpawners = AstroSpawners.getInstance().getConfig().getBoolean("allow-spawner-placement");


        if (!allowSpawners) {
            Bukkit.broadcastMessage(CC.translate(""));
            Bukkit.broadcastMessage(CC.translate("&a&l** SPAWNER PLACEMENT HAS BEEN ENABLED **"));
            Bukkit.broadcastMessage(CC.translate("&7&o( You can now place spawners again for FTOP value. )"));
            Bukkit.broadcastMessage(CC.translate(""));
            AstroSpawners.getInstance().getConfig().set("allow-spawner-placement", true);
            AstroSpawners.getInstance().saveConfig();
        } else {

            Bukkit.broadcastMessage(CC.translate(""));
            Bukkit.broadcastMessage(CC.translate("&c&l** SPAWNER PLACEMENT HAS BEEN DISABLED **"));
            Bukkit.broadcastMessage(CC.translate("&7&o( You can no longer place spawners for FTOP value anymore. )"));
            Bukkit.broadcastMessage(CC.translate(""));
            AstroSpawners.getInstance().getConfig().set("allow-spawner-placement", false);
            AstroSpawners.getInstance().saveConfig();
        }
        return true;
    }
}
