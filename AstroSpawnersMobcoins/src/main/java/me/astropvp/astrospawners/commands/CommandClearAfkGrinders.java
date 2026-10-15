package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandClearAfkGrinders implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) return false;
        AstroSpawners.getInstance().getAfkGrinders().clear();
        AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().clear();
        commandSender.sendMessage(CC.Red + "Cleared all afk grinder maps");
        return true;
    }
}
