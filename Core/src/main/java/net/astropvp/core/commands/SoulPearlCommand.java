package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoulPearlCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player pSender = (Player) commandSender;
        pSender.getInventory().addItem(AstroCore.getInstance().getSoulPearl());
        pSender.sendMessage(CC.translate("&aYou have received a soulpearl!"));
        return true;
    }
}
