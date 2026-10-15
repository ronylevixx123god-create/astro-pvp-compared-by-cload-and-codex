/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.earth2me.essentials.User
 *  com.earth2me.essentials.UserMap
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches.balancetop;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandBalanceTop
        implements CommandExecutor {
    static DecimalFormat df = new DecimalFormat("#,##0");

    public static void loadAllBalances() {
        final Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        new BukkitRunnable() {

            public void run() {
                int total = 0;
                long start = System.currentTimeMillis();
                UserMap map = ess.getUserMap();
                int loaded = 0;
                for (UUID uuid : map.getAllUniqueUsers()) {
                    OfflinePlayer offline;
                    User user = ess.getUser(uuid);
                    if (++total % 5000 == 0) {
                        Bukkit.getLogger().info("Scanned " + total + " total users...");
                    }
                    if ((offline = Bukkit.getOfflinePlayer(uuid)).getName() == null) continue;
                    if (!BalanceTop.getEconomy().hasAccount(offline)) {
                        System.out.println("No account for " + uuid.toString() + " Name: " + user.getName());
                        continue;
                    }
                    ++loaded;
                    double balance = BalanceTop.getEconomy().getBalance(offline);
                    if (!(balance >= 0.0)) continue;
                    BalanceManager.balanceMap.put(offline.getUniqueId(), new NameDouble(user.getName(), balance));
                }
                BalanceManager.lastupdate = System.currentTimeMillis();
                System.out.println("Took " + (System.currentTimeMillis() - start) + "ms to load " + loaded + " accounts out of " + total);
            }
        }.runTaskAsynchronously(Arkkit.get());
    }

    public static String insertCommas(double d) {
        return df.format(d);
    }

    public boolean onCommand(final CommandSender sender, Command arg1, String arg2, final String[] args) {
        if (sender.isOp() && args.length == 1 && (args[0].equalsIgnoreCase("recalc") || args[0].equalsIgnoreCase("recalculateusers"))) {
            if (args[0].equalsIgnoreCase("recalc")) {
                BalanceManager.lastupdate = System.currentTimeMillis();
                BalanceManager.getPages();
            } else {
                sender.sendMessage(ChatColor.RED + "Loading all users...");
                CommandBalanceTop.loadAllBalances();
            }
            return true;
        }
        if (sender.isOp() && args.length == 3 && args[0].equalsIgnoreCase("setbal")) {
            String pName = args[1];
            double d = Double.parseDouble(args[2]);
            UUID pUUID = null;
            for (Map.Entry<UUID, NameDouble> data : BalanceManager.balanceMap.entrySet()) {
                if (!data.getValue().getName().equalsIgnoreCase(pName)) continue;
                pUUID = data.getKey();
                break;
            }
            if (pUUID != null) {
                BalanceManager.balanceMap.put(pUUID, new NameDouble(pName, d));
                sender.sendMessage("Set /baltop cache value for " + pName + " (" + pUUID + ") to $" + d);
            } else {
                sender.sendMessage("Failed to locate cached /baltop data for " + pName + "!");
            }
            return true;
        }
        new BukkitRunnable() {

            public void run() {
                LinkedList<List<String>> pageCopy;
                sender.sendMessage("");
                int index = 1;
                if (args.length == 1 && StringUtils.isNumeric(args[0])) {
                    index = Math.max(1, Integer.parseInt(args[0]));
                }
                if (index > (pageCopy = BalanceManager.getPages()).size()) {
                    sender.sendMessage(ChatColor.RED + "Please provide a valid page number 1 - " + (pageCopy.size() + 1));
                    return;
                }
                List<String> page = pageCopy.get(index - 1);
                sender.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Top Balances (" + ChatColor.WHITE + ChatColor.BOLD + index + ChatColor.GREEN + ChatColor.BOLD + "/" + ChatColor.WHITE + ChatColor.BOLD + pageCopy.size() + ChatColor.GREEN + ChatColor.BOLD + ")");
                for (String s : page) {
                    sender.sendMessage(s);
                }
                sender.sendMessage("");
            }
        }.runTaskAsynchronously(Arkkit.get());
        return false;
    }
}

