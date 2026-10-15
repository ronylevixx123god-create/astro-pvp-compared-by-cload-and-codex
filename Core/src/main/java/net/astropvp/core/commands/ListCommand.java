package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        p.sendMessage("");
        p.sendMessage(CC.translate("&6&lAstro&f&lPvP List &7[" + Bukkit.getOnlinePlayers().size() + "&7 Online]"));
        p.sendMessage("");
        return false;
    }
}
