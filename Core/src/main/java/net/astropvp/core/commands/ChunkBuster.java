package net.astropvp.core.commands;

import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.ChunkBusterUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChunkBuster implements CommandExecutor {

    private AstroCore plugin;

    public ChunkBuster(AstroCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if ((!commandSender.isOp()) || (!(commandSender instanceof Player))) {
            return true;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.getInventory().addItem(new ItemStack[]{ChunkBusterUtils.getItem()});
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /givechunkbuster <name> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Invalid player.");
            return true;
        }
        try {
            ItemStack item = ChunkBusterUtils.getItem().clone();
            item.setAmount(Integer.parseInt(args[1]));
            player.getInventory().addItem(new ItemStack[]{item});
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Usage: /givechunkbuster <name> <amount>");
        }
        return true;
    }
}
