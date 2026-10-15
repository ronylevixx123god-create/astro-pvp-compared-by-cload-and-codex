package net.astropvp.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(!player.hasPermission("essentials.tp"))
            return true;
        List<Player> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.isOp() && !p.hasPermission("astrocore.staff"))
                players.add(p);
        }
        player.teleport(players.get(new Random().nextInt(players.size())));
        return false;
    }
}
