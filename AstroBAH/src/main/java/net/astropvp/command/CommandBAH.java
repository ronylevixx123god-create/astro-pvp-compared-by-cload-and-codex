//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.command;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.astropvp.auction.AuctionManager;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandBAH implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("history")) {
            ((Player) sender).openInventory(new net.astropvp.menu.AuctionHistoryMenu(1).getInventory());
            return true;
        }
        if (sender.isOp() || sender.hasPermission("decimatebah.admin")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("forcestart")) {
                if (AuctionManager.get().getCurrentAuction() != null) {
                    sender.sendMessage(CC.translate("&c&l(!) &cThere is already an active auction."));
                    return true;
                }
                if (!AuctionManager.get().hasAuctionItem()) {
                    sender.sendMessage(CC.translate("&cConfigure loot with /bah additem or setnextauction first."));
                    return true;
                }
                BlackMarketAuction.get().getAuctionManager().startRandomAuction();
                sender.sendMessage(ChatColor.GREEN + "Success.");
                return true;
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("setnextauction")) {
                final Player player = (Player)sender;
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    sender.sendMessage(CC.translate("&c&l(!) &cYou must be holding an item to use this."));
                    return true;
                }
                BlackMarketAuction.get().setNextAuctionItem(player.getItemInHand());
                sender.sendMessage(CC.translate("&aSuccess."));
                return true;
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("removenextauction")) {
                BlackMarketAuction.get().setNextAuctionItem(null);
                sender.sendMessage(CC.translate("&aSuccess."));
                return true;
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("additem")) {
                if(((Player) sender).getItemInHand() != null && ((Player) sender).getItemInHand().getType() != Material.AIR) {
                    if(!AuctionManager.get().addPossibleItem(((Player) sender).getItemInHand())) {
                        sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Failed to add item.");
                        return false;
                    }
                } else {
                    sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Cannot add null item.");
                    return false;
                }
                sender.sendMessage(CC.translate("&aSuccess."));
                return true;
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("removeitem")) {
                if(((Player) sender).getItemInHand() != null && ((Player) sender).getItemInHand().getType() != Material.AIR) {
                    if(!AuctionManager.get().removePossibleItem(((Player) sender).getItemInHand())) {
                        sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Failed to remove item.");
                        return false;
                    }
                } else {
                    sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Cannot remove null item.");
                    return false;
                }
                sender.sendMessage(CC.translate("&aSuccess."));
                return true;
            }
        }
        ((Player)sender).openInventory(BlackMarketAuction.get().getAuctionMenu().getInventory());
        return true;
    }
}
