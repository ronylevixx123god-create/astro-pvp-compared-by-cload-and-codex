/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.cosmicjackpot.struct;

import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.EconomyResponse;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Jackpot {
    public static transient double TAX = 0.1;
    private Map<PlayerInfo, Integer> entries = new ConcurrentHashMap<PlayerInfo, Integer>();
    private long totalWinnings = 1;
    private int secondsLeft;

    public String toString() {
        return this.totalWinnings + " - " + this.secondsLeft + " " + this.entries.size() + " entries.";
    }

    public Jackpot() {
    }

    public Jackpot(int timer) {
        this.secondsLeft = timer;
    }

    public void purchaseTicket(Player player, int tickets, int adjustedTickets) {
        double cost = tickets * this.getTicketPrice();
        EconomyResponse removed = Arkkit.economy.withdrawPlayer(player, cost);
        if (!removed.transactionSuccess()) {
            Bukkit.getLogger().info("Transaction not successful for " + player.getName() + " for $" + cost + " with " + tickets + " tickets purchased!");
            return;
        }
        PlayerInfo info = new PlayerInfo(player);
        Integer current = this.entries.get(info);
        Bukkit.getLogger().info("(CosmicJackpot) Purchasing " + tickets + " ticket(s) for " + player.getName() + (current != null ? " with " + current + " current total." : "0 current") + " adjusted=" + adjustedTickets);
        if (adjustedTickets != tickets) {
            tickets = adjustedTickets;
        }
        this.entries.put(info, (current == null ? 0 : current) + tickets);
        this.totalWinnings = (long)((double)this.totalWinnings + cost);
    }

    public double getPercentHolder(Player player) {
        return this.getPercentHolder(player.getUniqueId());
    }

    public double getPercentHolder(UUID uuid) {
        int ticketsOwned = this.getTicketsPurchased(uuid);
        if (ticketsOwned <= 0) {
            return 0.0;
        }
        int totalTickets = this.getTicketsSold();
        if (totalTickets <= 0) {
            return 0.0;
        }
        return (double)ticketsOwned / (double)totalTickets * 100.0;
    }

    public int getTicketsPurchased(Player player) {
        return this.getTicketsPurchased(player.getUniqueId());
    }

    public int getTicketsPurchased(UUID uuid) {
        int found = 0;
        for (Map.Entry<PlayerInfo, Integer> entries : this.entries.entrySet()) {
            if (!entries.getKey().getUuid().equals(uuid)) continue;
            found += entries.getValue().intValue();
        }
        return found;
    }

    public int getTicketPrice() {
        return 10000;
    }

    public int getTicketsSold() {
        int total = 0;
        for (Integer val : this.entries.values()) {
            total += val.intValue();
        }
        return total;
    }

    public long getPlayerWinnings() {
        long taxes = (long)((double)this.totalWinnings * TAX);
        return this.totalWinnings - taxes;
    }

    public PlayerInfo getRandomWinner() {
        if (this.entries.isEmpty()) {
            return null;
        }
        ArrayList winnerPool = Lists.newArrayList();
        this.entries.forEach((info, values) -> {
            for (int i = 0; i < values; ++i) {
                winnerPool.add(info);
            }
        });
        Collections.shuffle(winnerPool);
        return (PlayerInfo)winnerPool.get(ThreadLocalRandom.current().nextInt(winnerPool.size()));
    }

    public Map<PlayerInfo, Integer> getEntries() {
        return this.entries;
    }

    public long getTotalWinnings() {
        return this.totalWinnings;
    }

    public void setTotalWinnings(long totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public int getSecondsLeft() {
        return this.secondsLeft;
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }
}

