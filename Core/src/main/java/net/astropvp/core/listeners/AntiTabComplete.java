package net.astropvp.core.listeners;

import net.astropvp.core.AstroCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AntiTabComplete implements TabCompleter {
    private final List<String> subcommands = new ArrayList<>();

    public AntiTabComplete(AstroCore plugin) {
        subcommands.add("");

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        switch (args.length) {
            case 1:
                return subcommands;
            default:
                return new ArrayList<>();
        }

    }


}
