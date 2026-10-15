package net.astropvp.christmas.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;

import net.astropvp.christmas.ChristmasPlugin;
import net.astropvp.christmas.inventories.ChristmasInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChristmasCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player)commandSender;
        if (!player.isOp() || args.length == 0) {
            ChristmasInventory christmasInventory = new ChristmasInventory();
            christmasInventory.getGui().open(player);
            player.sendMessage(CC.translate((String)"&a&l(!)&a Opening the Christmas Present Menu."));
            return true;
        }
        if (args[0].equalsIgnoreCase("setitem") && args.length == 1) {
            ChristmasPlugin.getInstance().getChristmasManager().setCurrentItem(player.getItemInHand().clone());
            ChristmasPlugin.getInstance().getChristmasManager().clearAlreadyBought();
            player.sendMessage(CC.translate("&a&l(!)&a You have restocked the santa reward!"));
            Bukkit.broadcastMessage((String)"");
            Bukkit.broadcastMessage((String)StringUtils.getCenteredMessage((String)(CC.DarkGreenB + CC.Underline + "Christmas Shop")));
            Bukkit.broadcastMessage((String)"");
            Bukkit.broadcastMessage((String)StringUtils.getCenteredMessage((String)(CC.Gray + "The " + CC.DarkGreen + "Christmas Shop " + CC.Gray + "has been " + CC.DarkGreen + "restocked")));
            Bukkit.broadcastMessage((String)(StringUtils.getCenteredMessage((String)(CC.Gray + "with " + player.getItemInHand().getItemMeta().getDisplayName())) + CC.Gray + " type /santa to claim it."));
            Bukkit.broadcastMessage((String)"");
            return true;
        }
        return false;
    }
}
