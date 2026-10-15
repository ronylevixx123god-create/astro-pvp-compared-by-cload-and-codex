package net.astropvp.damagehandler.commands;


import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDebugDamage implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (sender instanceof Player && sender.isOp()) {
            if (AstroDamageHandler.getInstance().getConfig().getBoolean("debug-enabled")) {
                AstroDamageHandler.getInstance().getConfig().set("debug-enabled", false);
                AstroDamageHandler.getInstance().saveConfig();
                AstroDamageHandler.getInstance().reloadConfig();

                sender.sendMessage(CC.Red + "Damage debug disabled!");
            } else {
                AstroDamageHandler.getInstance().getConfig().set("debug-enabled", true);
                AstroDamageHandler.getInstance().saveConfig();
                AstroDamageHandler.getInstance().reloadConfig();

                sender.sendMessage(CC.Green + "Damage debug enabled!");
            }
            return true;
        }
        return false;
    }
}
