package net.astropvp.armorsets.commands;

import net.astropvp.armorsets.gui.SetGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOpenGUI implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        SetGUI gui = new SetGUI(player, "Armor Sets");
        gui.openBaseGUI();
        return false;
    }

}
