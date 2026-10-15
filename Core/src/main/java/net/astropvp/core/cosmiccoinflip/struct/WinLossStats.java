/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.cosmiccoinflip.struct;

public class WinLossStats {
    private int wins;
    private int dailyWins;
    private long winnings = 0L;
    private long dailyWinnings;
    private int losses;
    private int dailyLosses;
    long losings = 0L;
    long dailyLosings;
    long taxesPaid;

    public WinLossStats() {
        this(0, 0, 0L, 0L, 0, 0, 0L, 0L, 0L);
    }

    public void clearDailyStats() {
        this.dailyLosings = 0L;
        this.dailyWinnings = 0L;
        this.dailyWins = 0;
        this.dailyLosses = 0;
    }

    public WinLossStats(int wins, int dailyWins, long winnings, long dailyWinnings, int losses, int dailyLosses, long losings, long dailyLosings, long taxesPaid) {
        this.wins = wins;
        this.dailyWins = dailyWins;
        this.winnings = winnings;
        this.dailyWinnings = dailyWinnings;
        this.losses = losses;
        this.dailyLosses = dailyLosses;
        this.losings = losings;
        this.dailyLosings = dailyLosings;
        this.taxesPaid = taxesPaid;
    }

    public int getWins() {
        return this.wins;
    }

    public int getDailyWins() {
        return this.dailyWins;
    }

    public long getWinnings() {
        return this.winnings;
    }

    public long getDailyWinnings() {
        return this.dailyWinnings;
    }

    public int getLosses() {
        return this.losses;
    }

    public int getDailyLosses() {
        return this.dailyLosses;
    }

    public long getLosings() {
        return this.losings;
    }

    public long getDailyLosings() {
        return this.dailyLosings;
    }

    public long getTaxesPaid() {
        return this.taxesPaid;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDailyWins(int dailyWins) {
        this.dailyWins = dailyWins;
    }

    public void setWinnings(long winnings) {
        this.winnings = winnings;
    }

    public void setDailyWinnings(long dailyWinnings) {
        this.dailyWinnings = dailyWinnings;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setDailyLosses(int dailyLosses) {
        this.dailyLosses = dailyLosses;
    }

    public void setLosings(long losings) {
        this.losings = losings;
    }

    public void setDailyLosings(long dailyLosings) {
        this.dailyLosings = dailyLosings;
    }

    public void setTaxesPaid(long taxesPaid) {
        this.taxesPaid = taxesPaid;
    }
}

