package me.venir.astrocollectors.commands;

import me.venir.astrocollectors.collector.CollectorUtils;
import me.venir.astrocollectors.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CollectorGiveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            Language.COLLECTOR_HELP.send(sender);
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        int amount = Integer.parseInt(args[1]);

        player.getInventory().addItem(CollectorUtils.createCollector(amount));
        return true;
    }
}
