/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.MineBans.api.MineBanAPI
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicweblink.CosmicWebLinkAPI
 *  me.ifamasssxd.cosmicweblink.net.astropvp.armorsets.struct.DataTypeEnum
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.craftbukkit.libs.com.google.gson.Gson
 *  org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches.balancetop;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceManager {
    private static final DecimalFormat format;
    private static final HashSet<UUID> bannedCache;
    private static final DecimalFormat df;
    public static ConcurrentHashMap<UUID, NameDouble> balanceMap = new ConcurrentHashMap<>();
    public static Gson gson = new Gson();
    public static volatile LinkedList<List<String>> pages;
    public static long lastupdate;
    private static File jsonFile;
    private static LinkedHashMap<String, Double> balancetop;
    private static String topName;
    private static double topBalance;
    private static final boolean minebansEnabled;

    static {
        balancetop = null;
        pages = new LinkedList();
        lastupdate = System.currentTimeMillis();
        minebansEnabled = false;
        format = new DecimalFormat("#,###");
        bannedCache = new HashSet();
        df = new DecimalFormat("#,##0");
    }

    public static void setBalance(UUID uuid, String name, double cash) {
        balanceMap.put(uuid, new NameDouble(name, cash));
    }

    public static LinkedList<List<String>> getPages() {
        if (balancetop == null || lastupdate <= System.currentTimeMillis()) {
            System.out.println("Updating /baltop...");
            long start = System.currentTimeMillis();
            lastupdate = System.currentTimeMillis() + 1200000L;
            BalanceManager.loadOnlinePlayers();
            balancetop = BalanceManager.sortByComparator(balanceMap, false);
            pages.clear();
            ArrayList<String> page = new ArrayList<>();
            int i = 1;
            for (Map.Entry<String, Double> v : balancetop.entrySet()) {
                if (i == 1 && !v.getKey().equals(topName)) {
                    topBalance = v.getValue();
                    topName = v.getKey();
                    Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.UNDERLINE + v.getKey() + ChatColor.GREEN + " has taken the " + ChatColor.GREEN + ChatColor.BOLD + "#1" + ChatColor.GREEN + " /baltop with " + ChatColor.GREEN + ChatColor.BOLD + "$" + ChatColor.GREEN + format.format(v.getValue()) + "!");
                }
                if (pages.size() >= 20) break;
                if (page.size() >= 10) {
                    pages.add(new ArrayList<>(page));
                    page.clear();
                }
                page.add(ChatColor.WHITE + ChatColor.BOLD.toString() + i++ + ". " + ChatColor.GREEN + v.getKey() + ": " + ChatColor.WHITE + ChatColor.BOLD + "$" + BalanceManager.insertCommas(v.getValue()));
            }
            if (pages.size() < 20) {
                pages.add(page);
            }
            System.out.println("Updated /baltop in " + (System.currentTimeMillis() - start) + "ms");
        }
        return pages;
    }

    private static void loadOnlinePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            double cash = BalanceTop.getEconomy().getBalance(p);
            if (cash <= 0.0 && balanceMap.containsKey(p.getUniqueId())) {
                balanceMap.remove(p.getUniqueId());
                continue;
            }
            if (!(cash > 0.0)) continue;
            balanceMap.put(p.getUniqueId(), new NameDouble(p.getName(), cash));
        }
    }

    private static LinkedHashMap<String, Double> sortByComparator(Map<UUID, NameDouble> unsortMap, final boolean order) {
        LinkedList<Map.Entry<UUID, NameDouble>> list = new LinkedList<Map.Entry<UUID, NameDouble>>(unsortMap.entrySet());
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().getAmount().compareTo(o2.getValue().getAmount());
            }
            return o2.getValue().getAmount().compareTo(o1.getValue().getAmount());
        });
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<UUID, NameDouble> entry : list) {
            if (BalanceManager.isBanned(entry.getKey()) || entry.getValue().getName() != null && entry.getValue().getName().startsWith("faction_")) {
                balanceMap.remove(entry.getKey());
                continue;
            }
            sortedMap.put(entry.getValue().getName(), entry.getValue().getAmount());
        }
        return sortedMap;
    }

    private static boolean isBanned(UUID uuid) {
        return bannedCache.contains(uuid);
    }

    public static void loadBalances() {
        System.out.println("Loading balances..");
        long start = System.currentTimeMillis();
        try {
            File dir = Arkkit.get().getDataFolder();
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!(jsonFile = new File(Arkkit.get().getDataFolder(), "balances.json")).exists()) {
                jsonFile.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            try {
                StringBuilder build = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    build.append(line);
                }
                try {
                    balanceMap = gson.fromJson(build.toString(), new TypeToken<ConcurrentHashMap<UUID, NameDouble>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                    Bukkit.getLogger().info("UNABLE TO LOAD BALANCE MAP, POSSIBLE OUTDATED?");
                }
                if (balanceMap == null) {
                    balanceMap = new ConcurrentHashMap();
                }
                System.out.println("Took " + (System.currentTimeMillis() - start) + "ms to load " + BalanceManager.insertCommas(balanceMap.size()) + " balances.");
                new BukkitRunnable() {

                    public void run() {
                        if (balanceMap.isEmpty()) {
                            Bukkit.getLogger().info("Syncing all users due to empty balance map...");
                            CommandBalanceTop.loadAllBalances();
                        }
                    }
                }.runTaskLaterAsynchronously(Arkkit.get(), 1L);
            } finally {
                if (Collections.singletonList(reader).get(0) != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String insertCommas(double d) {
        return df.format(d);
    }

    public static void saveBalances() {
        try {
            String map = gson.toJson(balanceMap);
            FileWriter writer = new FileWriter(jsonFile);
            try {
                writer.write(map);
                writer.flush();
            } finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

