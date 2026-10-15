package net.astropvp.core.conaxgames.staff;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FreezeCommand extends Command {

    public FreezeCommand() {
        super("freeze");
        setAliases(Arrays.asList("ss", "screenshare"));
        setUsage(ChatColor.RED + "/freeze");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Player command.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("astrocore.freeze")) {
            player.sendMessage(CC.Red + "No Permission.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(CC.Red + "/ss <target>");
            return false;
        }

        if (args[0] != null) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(CC.Red + "Can't find target or target was not online.");
                return false;
            }
            Freeze.handle(target, player);
        }
        return false;
    }
}
