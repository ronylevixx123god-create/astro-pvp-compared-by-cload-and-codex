package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveSpawnersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No Perms.");
            return false;
        }

        for (String key : AstroSpawners.getInstance().getSpawnerManager().getCacheKeys()) {
            sender.sendMessage(key);
        }
        AstroSpawners.getInstance().getSpawnerManager().purge();

        AstroSpawners.getInstance().getSpawnerManager().saveAll();
        sender.sendMessage(CC.Green + "Done saving!");
        return true;
    }
}
