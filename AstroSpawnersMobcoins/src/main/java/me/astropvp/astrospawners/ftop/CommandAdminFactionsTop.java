package me.astropvp.astrospawners.ftop;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandAdminFactionsTop implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!sender.hasPermission("ftop.recalculate"))
            return false;
        FactionsTopAPI.recalculateFactionsTop();
        sender.sendMessage(CC.GreenB + "(!) " + CC.Green + "Forcefully recalculated F-Top!");
        return false;
    }
}
