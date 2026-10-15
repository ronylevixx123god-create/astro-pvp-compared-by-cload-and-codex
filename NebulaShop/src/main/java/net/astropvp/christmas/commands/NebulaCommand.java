package net.astropvp.christmas.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import me.matthawkk.chat.messages.PrefixedMessage;
import net.astropvp.christmas.NebulaShop;
import net.astropvp.christmas.inventories.NebulaShopInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NebulaCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player))
            return true;
        Player player = (Player) commandSender;
        if(!player.hasPermission("astrocore.nebula")) {
            player.sendMessage("");
            player.sendMessage(CC.translate("&c&l(!)&c You cannot use the \"/nebula\" command."));
            player.sendMessage(CC.translate("&7Purchase the nebula rank on &f&nbuy.astropvp.net&7!"));
            player.sendMessage("");
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("shop")) {
            new NebulaShopInventory(player).open(player);
            player.sendMessage(CC.translate("&5&l<&d&lNebula&5&l> &d") + "Opening Nebula Shop.");
            return true;
        }

        if(!player.isOp() || args.length == 0) {
            player.teleport(NebulaShop.getInstance().getNebulaShopManager().getShopLocation());
            player.sendMessage(CC.translate("&a&l(!)&a Teleporting to Nebula Lounge..."));
            return true;
        }
        if(args[0].equalsIgnoreCase("setitem") && args.length == 2) {
            NebulaShop.getInstance().getNebulaShopManager().setCurrentItem(player.getItemInHand().clone());
            NebulaShop.getInstance().getNebulaShopManager().setCurrentPrice(Double.parseDouble(args[1]));
            NebulaShop.getInstance().getNebulaShopManager().clearAlreadyBought();
            player.sendMessage(CC.Green + "Success!");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.LightPurpleB + CC.Underline + "Nebula Shop"));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.Gray + "The " + CC.LightPurple + "Nebula Shop " + CC.Gray + "has been " + CC.LightPurple + "restocked"));
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.Gray + "with " + player.getItemInHand().getItemMeta().getDisplayName()) + CC.Gray + " for " + CC.LightPurple + "$ " +NumberUtils.formatMoney(Double.parseDouble(args[1])));
            Bukkit.broadcastMessage("");
            return true;
        }
        if(args[0].equalsIgnoreCase("setspawn")) {
            NebulaShop.getInstance().getNebulaShopManager().setShopLocation(player.getLocation());
            player.sendMessage(CC.Green + "Success!");
            return true;
        }





        return false;
    }
}
