package net.astropvp.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import org.apache.commons.lang.*;
import org.bukkit.inventory.*;
import java.util.*;

public class GiveallCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player)sender;

        if (!sender.hasPermission("astro.giveall")) {
            sender.sendMessage(CC.Red + "No permission.");
        }
        else if (args.length == 0 || !args[0].equalsIgnoreCase("hand")) {
            sender.sendMessage(CC.translate("&c&l(!) &cUsage: /giveall <hand> <alt-limit>"));
        }

        else if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(CC.translate("&c&l(!) &cYou must be holding an item to give all."));
        }

        else {
            int altLimit = 1;
            if (args.length >= 2) {
                try {
                    altLimit = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException e) {
                    player.sendMessage(CC.translate("&c&l(!) &cInvalid alt limit number."));
                    return false;
                }
            }
            final ItemStack itemToGive = player.getItemInHand();
            final Map<String, Integer> ipMap = new HashMap<String, Integer>();
            for (final Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    continue;
                }
                final String ip = p.getAddress().getAddress().toString();
                if (ipMap.containsKey(ip) && ipMap.get(ip) > altLimit) {
                    continue;
                }
                String displayName = itemToGive.hasItemMeta() ? itemToGive.getItemMeta().getDisplayName() : null;
                if (displayName == null) {
                    displayName = WordUtils.capitalizeFully(itemToGive.getType().name().replace("_", " "));
                }
                p.sendMessage(CC.translate("&8[&6&lAstro&f&lPvP&8] &7You have received " + itemToGive.getAmount() + "x " + displayName + "&7 from a give all."));
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), itemToGive);
                }
                else {
                    p.getInventory().addItem(new ItemStack[] { itemToGive });
                }
                ipMap.put(ip, ipMap.getOrDefault(ip, 0) + 1);
            }
            player.sendMessage(CC.translate("&a&l(!) &cSuccessfully gave all. " + itemToGive.getType()));
        }
        return true;
    }

}
