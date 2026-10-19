//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import net.astropvp.menu.AuctionLootMenu;

public class CommandBAHLoot implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command requires a player.");
            return true;
        }
        if (strings.length == 0 || !strings[0].equalsIgnoreCase("edit")) {
            ((Player) commandSender).openInventory(new net.astropvp.menu.AuctionLootPreviewMenu(1).getInventory());
            return true;
        }
        if (!commandSender.isOp() && !commandSender.hasPermission("decimatebah.admin")) {
            commandSender.sendMessage("You do not have permission to edit auction loot.");
            return true;
        }
        if (com.decimatemc.blackmarketauction.BlackMarketAuction.get().getAuctionManager().externalLootSelected()) {
            net.astropvp.loottable.guis.EcoWeeksGUI editor = new net.astropvp.loottable.guis.EcoWeeksGUI(
                    (Player) commandSender, net.astropvp.loottable.struct.LootTable.BAH);
            editor.getEcoWeeksGUI().open((Player) commandSender);
            return true;
        }
        ((Player) commandSender).openInventory(new AuctionLootMenu().getInventory());
        return true;
    }
}
