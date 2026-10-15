package net.astropvp.commands;

import net.astropvp.CollectPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CollectLoadCommand implements CommandExecutor {

    private final CollectPlugin collectPlugin;

    public CollectLoadCommand(CollectPlugin collectPlugin) {
        this.collectPlugin = collectPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;


        Player player = (Player) sender;

        if (!player.isOp()) return true;


        this.collectPlugin.getCollectManager().loadAll();
        return true;
    }
}
