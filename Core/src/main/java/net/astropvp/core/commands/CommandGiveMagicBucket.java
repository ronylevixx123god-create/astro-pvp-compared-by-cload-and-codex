package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.items.MagicalLavaBucket;
import net.astropvp.core.items.MagicalWaterBucket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveMagicBucket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player)commandSender;
        if (args.length != 2) {
            player.sendMessage(CC.translate("&c/givebucket (LAVA/WATER) (uses)"));
        }
        if (args[0].equals("LAVA")) {
            int uses = Integer.parseInt(args[1]);
            player.getInventory().addItem(MagicalLavaBucket.createMagicalLavaBucket(uses));
        } else if (args[0].equals("WATER")) {
            int uses = Integer.parseInt(args[1]);
            player.getInventory().addItem(MagicalWaterBucket.createMagicalWaterBucket(uses));
        }
        player.sendMessage(CC.translate("&aAdded magic bucket to inventory!"));
        return true;
    }
}
