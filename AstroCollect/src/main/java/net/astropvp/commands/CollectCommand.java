package net.astropvp.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.inventories.CollectInventory;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CollectCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;


        Player player = (Player) sender;

        if (CombatLog.inCombat(player)) {
            player.sendMessage(CC.translate("&c&l(!)&c You cannot type /collect in combat!"));
            return false;
        }


        new CollectInventory(player).getCollectUI().open(player);
        return false;
    }
}
