/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.cosmiccoinflip.commands;

import net.astropvp.core.cosmiccoinflip.struct.CoinFlipManager;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipMatch;
import net.astropvp.core.cosmiccoinflip.struct.WinLossStats;
import net.astropvp.core.AstroCore;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;

public class CommandCoinFlip
implements CommandExecutor {
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 2 && sender.isOp() && args[0].equalsIgnoreCase("setmax")) {
            try {
                int integer = Integer.parseInt(args[1]);
                AstroCore.getInstance().getCoinFlipManager().setAlertAmount(integer);
                AstroCore.getInstance().getConfig().set("alertAmount", integer);
                AstroCore.getInstance().saveConfig();
                sender.sendMessage(ChatColor.RED + "Set alert threshold to: " + integer);
            }
            catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid number!");
            }
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        CoinFlipManager manager = AstroCore.getInstance().getCoinFlipManager();
        Player player = (Player)sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Coin Flip Help");
                player.sendMessage(ChatColor.YELLOW + "/coinflip");
                player.sendMessage(ChatColor.GRAY + "View all active Coin Flip matches.");
                player.sendMessage(ChatColor.YELLOW + "/coinflip <amount>");
                player.sendMessage(ChatColor.GRAY + "Start a Coin Flip match with the given wager.");
                player.sendMessage(ChatColor.YELLOW + "/coinflip cancel");
                player.sendMessage(ChatColor.GRAY + "Cancel the pending Coin Flip match.");
                player.sendMessage(ChatColor.YELLOW + "/coinflip toggle");
                player.sendMessage(ChatColor.GRAY + "Toggle Coin Flip win notifications.");
                player.sendMessage(ChatColor.YELLOW + "/coinflip stats");
                player.sendMessage(ChatColor.GRAY + "View your current Coin Flip stats.");
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stfu") || args[0].equalsIgnoreCase("ignore")) {
                if (manager.getToggledNotifications().contains(player.getUniqueId())) {
                    manager.getToggledNotifications().remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "Coin Flip notifications turned " + ChatColor.GREEN + ChatColor.BOLD + "ON.");
                    manager.saveToggles();
                    return true;
                }
                manager.getToggledNotifications().add(player.getUniqueId());
                manager.saveToggles();
                player.sendMessage(ChatColor.GRAY + "Coin Flip notifications turned " + ChatColor.RED + ChatColor.BOLD + "OFF.");
                return true;
            }
            if (args[0].equalsIgnoreCase("stats")) {
                WinLossStats winLossStats = manager.getWinLossStats().get(player.getUniqueId());
                if (winLossStats == null) {
                    winLossStats = new WinLossStats();
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Coin Flip Stats");
                String format = ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + "%s" + ChatColor.GRAY + " - " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "$%s";
                sender.sendMessage(ChatColor.YELLOW + "Wins Today: " + String.format(format, winLossStats.getDailyWins(), this.decimalFormat.format(winLossStats.getDailyWinnings())));
                sender.sendMessage(ChatColor.YELLOW + "Losses Today: " + String.format(format, winLossStats.getDailyLosses(), this.decimalFormat.format(winLossStats.getDailyLosings())));
                sender.sendMessage(ChatColor.YELLOW + "Total Wins: " + String.format(format, winLossStats.getWins(), this.decimalFormat.format(winLossStats.getWinnings())));
                sender.sendMessage(ChatColor.YELLOW + "Total Losses: " + String.format(format, winLossStats.getLosses(), this.decimalFormat.format(winLossStats.getLosings())));
                sender.sendMessage(ChatColor.YELLOW + "Taxes Paid: " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "$" + this.decimalFormat.format(winLossStats.getTaxesPaid()));
                sender.sendMessage("");
                return true;
            }
            CoinFlipMatch match = manager.getMatch(player);
            if (match != null) {
                if (args[0].equalsIgnoreCase("cancel")) {
                    if (match.getMatchState() == CoinFlipMatch.MatchState.WAITING) {
                        AstroCore.getInstance().getEcon().depositPlayer(player, match.getWager());
                        match.setMatchState(CoinFlipMatch.MatchState.DONE);
                        manager.getCoinFlipMatches().remove(match);
                        player.sendMessage(ChatColor.RED + "You left your Coin Flip Match!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "You can not leave the match at this time.");
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "You are already in a Coin Flip Match!");
                return true;
            }
            String number = args[0].replace("k", "000").replace("m", "000000").replace("b", "000000000");
            if (StringUtils.isNumeric(number)) {
                double val = Double.parseDouble(number);
                if (val <= 0.0) {
                    sender.sendMessage(ChatColor.RED + "You must enter a valid amount to bet!");
                    return true;
                }
                if (!AstroCore.getInstance().getEcon().has(player, val)) {
                    sender.sendMessage(ChatColor.RED + "You do not have that much money to bet!");
                    return true;
                }
                if (val < 10000.0) {
                    sender.sendMessage(ChatColor.RED + "You must bet atleast $10,000!");
                    return true;
                }
                player.setMetadata("coinFlipWage", new FixedMetadataValue(AstroCore.getInstance(), val));
                Inventory flip = manager.getCoinFlipSelectMenu(val, null);
                player.openInventory(flip);
                return true;
            }
        } else {
            if (args.length == 0) {
                ((Player)sender).openInventory(manager.getLobbyInventory((Player)sender));
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "/coinflip");
            sender.sendMessage(ChatColor.WHITE + "View all active Coin Flip Matches.");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GREEN + "/coinflip <betAmount>");
            sender.sendMessage(ChatColor.WHITE + "Create a Coin Flip Match to vs another player!");
        }
        return true;
    }
}

