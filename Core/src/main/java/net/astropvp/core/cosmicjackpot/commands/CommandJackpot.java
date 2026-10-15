/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.cosmicjackpot.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.LinkedList;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.astropvp.core.cosmicjackpot.struct.Jackpot;
import net.astropvp.core.cosmicjackpot.struct.JackpotHistory;
import net.astropvp.core.cosmicjackpot.struct.JackpotManager;
import net.astropvp.core.cosmicjackpot.utils.TimeUtils;
import net.astropvp.core.AstroCore;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandJackpot
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        JackpotManager manager = AstroCore.getInstance().getJackpotManager();
        if ((args.length == 2 || args.length == 1) && args[0].equalsIgnoreCase("buy")) {
            Jackpot current = manager.getCurrentJackpot();
            if (args[0].equalsIgnoreCase("buy")) {
                String arg;
                Player player = (Player)sender;
                String string = arg = args.length == 2 ? args[1] : "1";
                if (!StringUtils.isNumeric(arg)) {
                    sender.sendMessage(ChatColor.RED + "Please enter a valid amount of tickets to purchase!");
                    return true;
                }
                int tickets = Integer.parseInt(arg);
                if (tickets < 1 || tickets > 1000000) {
                    sender.sendMessage(ChatColor.RED + "Please enter a ticket amount between 0 and 1,000,000!");
                    return true;
                }
                long totalPrice = (long) tickets * current.getTicketPrice();
                if (Arkkit.economy.getBalance(player) < totalPrice) {
                    sender.sendMessage(ChatColor.RED + "You cannot afford the total price of $" + manager.getMoneyFormat().format(totalPrice) + " for " + tickets + " tickets!");
                    return true;
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GREEN + "Confirm purchase of " + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE + tickets + ChatColor.GREEN + " Jackpot Ticket(s)");
                sender.sendMessage(ChatColor.GREEN + "for " + ChatColor.AQUA + ChatColor.BOLD.toString() + "$" + ChatColor.AQUA + manager.getMoneyFormat().format(totalPrice) + (tickets > 1 ? ChatColor.GREEN + " at " + ChatColor.AQUA + ChatColor.BOLD + "$" + ChatColor.AQUA + manager.getMoneyFormat().format(current.getTicketPrice()) + "/ea" : "!"));
                sender.sendMessage("");
                player.setMetadata("jackpotTicket", new FixedMetadataValue(AstroCore.getInstance(), tickets));
                player.openInventory(manager.getJackpotConfirmMenu(tickets, totalPrice));
                return true;
            }
        } else if (args.length == 1 || args.length == 2 && args[0].equalsIgnoreCase("top")) {
            if (args[0].equalsIgnoreCase("top")) {
                int startIndex;
                int totalPages;
                int page;
                LinkedList<JackpotHistory> topWinners = manager.getSortedJackpotHistory();
                int perPage = 15;
                if (topWinners.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "There are no recorded Jackpot winners!");
                    return true;
                }
                int n = page = args.length == 2 && StringUtils.isNumeric(args[1]) ? Integer.parseInt(args[1]) : 1;
                if (page < 0) {
                    page = 1;
                }
                if (page > (totalPages = (int)Math.min(5.0, Math.ceil((double)topWinners.size() / Double.valueOf(perPage))))) {
                    page = totalPages;
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Top Jackpot Winners (" + ChatColor.AQUA + ChatColor.BOLD.toString() + page + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "/" + ChatColor.AQUA + ChatColor.BOLD + totalPages + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + ")");
                for (int i = startIndex = page * perPage - perPage; i < startIndex + perPage && i < topWinners.size(); ++i) {
                    JackpotHistory history = topWinners.get(i);
                    int position = i + 1;
                    sender.sendMessage(" " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + position + ". " + ChatColor.WHITE + history.getName() + ChatColor.LIGHT_PURPLE + " - " + ChatColor.AQUA + ChatColor.BOLD.toString() + (int)history.getJackpotWins() + ChatColor.AQUA + " Wins" + ChatColor.AQUA + ChatColor.BOLD + " (" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "$" + ChatColor.LIGHT_PURPLE + manager.getMoneyFormat().format(history.getJackpotWinnings()) + ChatColor.AQUA + ChatColor.BOLD.toString() + ")");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("draw") && sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Forcing Jackpot winner!");
                try {
                    manager.drawWinner();
                } catch (UserDoesNotExistException e) {
                    e.printStackTrace();
                } catch (NoLoanPermittedException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("count") && sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Forcing countdown.");
                manager.getCurrentJackpot().setSecondsLeft(61);
                return true;
            }
            if (args[0].equalsIgnoreCase("stats")) {
                Player pl = (Player)sender;
                JackpotHistory history = manager.getJackpotHistory().get(pl.getUniqueId());
                if (history == null) {
                    history = new JackpotHistory(pl.getName());
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Astro Jackpot Stats");
                String moneyString = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "$" + ChatColor.LIGHT_PURPLE + "%s";
                sender.sendMessage(ChatColor.AQUA + "Total Winnings: " + String.format(moneyString, manager.getMoneyFormat().format(history.getJackpotWinnings())));
                sender.sendMessage(ChatColor.AQUA + "Total Tickets Purchased: " + ChatColor.LIGHT_PURPLE + manager.getMoneyFormat().format(history.getTicketsPurchased()));
                sender.sendMessage(ChatColor.AQUA + "Total Jackpot Wins: " + ChatColor.LIGHT_PURPLE + manager.getMoneyFormat().format(history.getJackpotWins()));
                sender.sendMessage("");
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("notifications") || args[0].equalsIgnoreCase("stfu")) {
                Player pl = (Player)sender;
                if (manager.getToggledNofications().remove(pl.getUniqueId())) {
                    pl.sendMessage(CC.Yellow + "Jackpot Notifications " + CC.GreenB + "ENABLED");
                } else {
                    manager.getToggledNofications().add(pl.getUniqueId());
                    pl.sendMessage(CC.Yellow + "Jackpot Notifications " + CC.RedB + "DISABLED");
                    pl.sendMessage(CC.Gray + "You will no longer see Jackpot countdown notifications!");
                }
                return true;
            }
        } else {
            if (args.length == 0) {
                Jackpot current = AstroCore.getInstance().getJackpotManager().getCurrentJackpot();
                if (current == null) {
                    sender.sendMessage(ChatColor.RED + "It seems there is no jackpot currently running!");
                    return true;
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Astro Jackpot");
                sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "  Jackpot Value: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "$" + ChatColor.LIGHT_PURPLE + manager.getMoneyFormat().format(current.getPlayerWinnings()) + ChatColor.GRAY + " (-" + (int)(Jackpot.TAX * 100.0) + "% tax)");
                sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "  Tickets Sold: " + ChatColor.YELLOW + manager.getMoneyFormat().format(current.getTicketsSold()));
                sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "  Your Tickets: " + ChatColor.GREEN + current.getTicketsPurchased((Player)sender) + ChatColor.GRAY + " (" + manager.getMoneyFormat().format(current.getPercentHolder((Player)sender)) + "%)");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "(!) " + ChatColor.AQUA + "Next Winner in " + ChatColor.AQUA + ChatColor.UNDERLINE + TimeUtils.formatDifference(current.getSecondsLeft()));
                sender.sendMessage("");
                return true;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("settime") && sender.isOp()) {
                int time = Integer.parseInt(args[1]);
                manager.setDefaultDrawTime(time);
                AstroCore.getInstance().getConfig().set("drawTime", (Object)time);
                AstroCore.getInstance().saveConfig();
                sender.sendMessage(ChatColor.RED + "Draw time set to " + time + " seconds.");
                return true;
            }
        }
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Jackpot Commands");
        sender.sendMessage(ChatColor.WHITE + "/jackpot");
        sender.sendMessage(ChatColor.GRAY + "View current Jackpot information.");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "/jackpot buy <x>");
        sender.sendMessage(ChatColor.GRAY + "Buy X tickets for the current Jackpot.");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "/jackpot top");
        sender.sendMessage(ChatColor.GRAY + "View top Jackpot Winners!");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "/jackpot stats");
        sender.sendMessage(ChatColor.GRAY + "View your Jackpot stats!");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "/jackpot notifications");
        sender.sendMessage(ChatColor.GRAY + "Toggle Jackpot countdown notifications!");
        sender.sendMessage("");
        return false;
    }
}

