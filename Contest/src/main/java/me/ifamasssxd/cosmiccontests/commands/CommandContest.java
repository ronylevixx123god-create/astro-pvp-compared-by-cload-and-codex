/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmiccontests.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.UUID;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.contests.CustomDataContest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestData;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContest
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            ContestManager manager = CosmicContests.get().getContestManager();
            manager.openContestInventory((Player)sender);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                CosmicContests.get().getPrizeManager().openPrizeClaimMenu((Player)sender);
                return true;
            }
            if (args[0].equalsIgnoreCase("fixtime") && sender.isOp()) {
                ContestManager manager = CosmicContests.get().getContestManager();
                manager.recalcLastReset();
                sender.sendMessage((Object)ChatColor.RED + "Recalculated last reset.. due to reset in " + TimeUtils.getNextResetTimer());
                return true;
            }
            if (args[0].equalsIgnoreCase("reroll") && sender.isOp()) {
                ContestManager manager = CosmicContests.get().getContestManager();
                manager.loadTopUsers(true);
                manager.clearUsersAndContests();
                sender.sendMessage((Object)ChatColor.RED + "Cleared current contests and active me.astropvp.cratekeys.data...");
                return true;
            }
            if (args[0].equalsIgnoreCase("list") && sender.isOp()) {
                ContestManager manager = CosmicContests.get().getContestManager();
                for (Contests contest : Contests.values()) {
                    if (contest.getContest() instanceof CustomDataContest) {
                        String key2 = ((CustomDataContest)contest.getContest()).getKey();
                        sender.sendMessage(CC.Gray + "[" + ((CustomDataContest)contest.getContest()).getName() + "] " + (manager.getActiveContests().contains((Object)contest) ? CC.Green : CC.White) + key2);
                        continue;
                    }
                    sender.sendMessage((manager.getActiveContests().contains((Object)contest) ? CC.Green : CC.White) + contest.name());
                }
                return true;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setresettime") && sender.isOp()) {
            long time = Long.parseLong(args[1]);
            CosmicContests.get().getContestManager().setLastMapResetTime(time);
            CosmicContests.get().getConfig().set("lastMapResetTime", (Object)time);
            CosmicContests.get().saveConfig();
            sender.sendMessage((Object)ChatColor.RED + "Last reset time set to " + time);
            return true;
        }
        if (!sender.isOp()) {
            return true;
        }
        if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("add")) {
                Player pl;
                double amount;
                Integer intAmount;
                String name = args[1];
                String contestName = args[2];
                try {
                    intAmount = Integer.parseInt(args[args.length == 5 ? 4 : 3]);
                    amount = intAmount.doubleValue();
                }
                catch (Exception e) {
                    try {
                        amount = Double.parseDouble(args[args.length == 5 ? 4 : 3]);
                    }
                    catch (Exception ex) {
                        sender.sendMessage((Object)ChatColor.RED + "Please enter a valid number!");
                        return true;
                    }
                    intAmount = null;
                }
                UUID given = null;
                if (name.contains("-")) {
                    try {
                        given = UUID.fromString(name);
                    }
                    catch (Exception ex) {
                        // empty catch block
                    }
                }
                Player player = pl = given != null ? Bukkit.getPlayer((UUID)given) : Bukkit.getPlayer((String)name);
                if (pl == null && given == null) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid online player!");
                    return true;
                }
                ContestUser user = ContestUser.getUser(given != null ? given : pl.getUniqueId());
                Contests inner = Contests.getContestFromInnerKey(contestName);
                if (inner == null) {
                    if (contestName.equals("broken")) {
                        inner = Contests.BLOCKS_MINED;
                    } else if (contestName.equals("placed")) {
                        inner = Contests.BLOCKS_PLACED;
                    }
                }
                if (inner != null && user != null) {
                    if (intAmount != null && !inner.isUseLargeStorage()) {
                        user.incrementIntAmount(inner, args.length == 5 ? args[3] : contestName, intAmount);
                    } else {
                        user.incrementValue(inner, args.length == 5 ? args[3] : contestName, amount);
                    }
                    if (intAmount != null) {
                        amount = intAmount.intValue();
                    }
                    if (sender instanceof Player || CosmicContests.get().getServerName() != null && CosmicContests.get().getServerName().contains("devplanet")) {
                        sender.sendMessage((Object)ChatColor.GREEN + "Added " + (intAmount != null ? (double)intAmount.intValue() : amount) + " to " + user.getName() + " - " + user.getData(inner, args.length == 5 ? args[3] : contestName));
                    }
                    return true;
                }
                if (inner == null) {
                    sender.sendMessage((Object)ChatColor.RED + "Contest not found for " + name + " Contest name: " + contestName);
                } else {
                    sender.sendMessage((Object)ChatColor.RED + "Invalid user!");
                }
            }
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("info")) {
            Player pl;
            String name = args[1];
            String contName = args[2];
            if (args.length > 3) {
                for (int i = 3; i < args.length; ++i) {
                    contName = contName + " " + args[i];
                }
                contName = contName.trim();
            }
            if ((pl = Bukkit.getPlayer((String)name)) == null) {
                sender.sendMessage((Object)ChatColor.RED + "That is not a valid online player!");
                return true;
            }
            Contests found = Contests.getFromName(contName);
            if (found == null) {
                sender.sendMessage((Object)ChatColor.RED + "Unable to find contest with that name!");
                sender.sendMessage((Object)ChatColor.GRAY + "Valid contest names: ");
                for (Contests cOntests : Contests.values()) {
                    sender.sendMessage((Object)ChatColor.GRAY + cOntests.getContest().getName());
                }
                return true;
            }
            ContestUser user = ContestUser.getUser(pl.getUniqueId());
            if (user == null) {
                sender.sendMessage((Object)ChatColor.RED + "No contest user found..");
                return true;
            }
            ContestData data = user.getContestData().get((Object)found);
            if (data == null || data.getData().isEmpty()) {
                sender.sendMessage((Object)ChatColor.RED + "No me.astropvp.cratekeys.data found for " + found.getContest().getName() + "!");
                return true;
            }
            sender.sendMessage((Object)ChatColor.GREEN + "Listing all me.astropvp.cratekeys.data for " + pl.getName() + "'s " + found.getContest().getName() + " Challenges");
            data.getData().forEach((key, obj) -> sender.sendMessage((Object)ChatColor.GRAY + "Key: " + key + " Data: " + obj));
            return true;
        }
        return false;
    }
}

