package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.items.ThrowableCreeperEggs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GiveThrowableCreeperEgg extends Command {

    public GiveThrowableCreeperEgg() {
        super("throwable");
        setAliases(Arrays.asList("tcegg", "throwablecegg"));
        setUsage(ChatColor.RED + "/tcegg <player> <amount>");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Player command.");
            return true;
        }

        Player sender = (Player) commandSender;

        if (!sender.hasPermission("astrocore.admin")) {
            sender.sendMessage(CC.Red + "No Permission.");
            return false;
        }

        Player player = Bukkit.getPlayer(strings[0]);

        if (player == null) {
            sender.sendMessage(CC.Red + "Invalid player name.");
            return true;
        }

        if (!isNumeric(strings[1])) {
            sender.sendMessage(CC.Red + "Invalid number.");
            return true;
        }

        int amount = Integer.parseInt(strings[1]);

        player.getInventory().addItem(ThrowableCreeperEggs.createThrowableCreeperEgg(amount));
        return false;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
