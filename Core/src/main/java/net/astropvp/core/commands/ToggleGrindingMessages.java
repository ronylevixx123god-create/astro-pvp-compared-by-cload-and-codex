package net.astropvp.core.commands;

import net.astropvp.core.utils.GrindingMessagesUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleGrindingMessages implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return false;
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /grinding stfu");
            return true;
        }
        Player player = (Player)sender;
        if(args[0].equalsIgnoreCase("stfu") || args[0].equalsIgnoreCase("toggle"))
        if (GrindingMessagesUtil.isPlayerIndicating(player)) {
            player.sendMessage("§eYou have §c§lDISABLED §eGrinding Messages!");
            GrindingMessagesUtil.removePlayer(player);
            return false;
        }
        player.sendMessage("§eYou have §a§lENABLED §eGrinding Messages!");
        GrindingMessagesUtil.addPlayer(player);
        return false;
    }
}
