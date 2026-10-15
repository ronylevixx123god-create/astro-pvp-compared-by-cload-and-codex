/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EconomyUtils {
    private static Economy economy;

    public static Economy getEconomy() {
        if (economy == null && (economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider()) == null) {
            Bukkit.getLogger().info("Unable to get economy!!");
            return null;
        }
        return economy;
    }

    public static boolean hasBalance(OfflinePlayer player, double balance) {
        return EconomyUtils.getEconomy().has(player, balance);
    }

    public static double depositBalanceAndGet(OfflinePlayer player, double balance) {
        EconomyUtils.getEconomy().depositPlayer(player, balance);
        return EconomyUtils.getEconomy().getBalance(player);
    }

    public static void depositBalance(OfflinePlayer player, double balance) {
        EconomyUtils.getEconomy().depositPlayer(player, balance);
    }

    public static double withdrawBalanceAndGet(OfflinePlayer player, double balance) {
        EconomyUtils.getEconomy().withdrawPlayer(player, balance);
        return EconomyUtils.getEconomy().getBalance(player);
    }

    public static void withdrawBalance(OfflinePlayer player, double balance) {
        EconomyUtils.getEconomy().withdrawPlayer(player, balance);
    }

    public static double getBalance(OfflinePlayer player) {
        return EconomyUtils.getEconomy().getBalance(player);
    }
}

