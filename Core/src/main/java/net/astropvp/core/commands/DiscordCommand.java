package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        p.sendMessage("");
        p.sendMessage(CC.translate("&6&lAstro&f&lPvP Discord"));
        p.sendMessage(CC.translate("&f&nhttps://discord.astropvp.net"));
        p.sendMessage(CC.translate("&7Click the link above to join the discord."));
        p.sendMessage("");
        return false;

    }

}