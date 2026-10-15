/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.cosmicjackpot.struct;

public class JackpotHistory
implements Comparable {
    private String name;
    private double jackpotWins;
    private long jackpotWinnings;
    private double ticketsPurchased;

    public JackpotHistory(String name) {
        this.name = name;
    }

    public JackpotHistory() {
    }

    public int compareTo(Object o) {
        if (o instanceof JackpotHistory) {
            JackpotHistory other = (JackpotHistory)o;
            if (other.getJackpotWinnings() == this.getJackpotWinnings() && other.getJackpotWins() > this.getJackpotWins()) {
                return 1;
            }
            if (other.getJackpotWinnings() > this.getJackpotWinnings()) {
                return 1;
            }
            return -1;
        }
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public double getJackpotWins() {
        return this.jackpotWins;
    }

    public long getJackpotWinnings() {
        return this.jackpotWinnings;
    }

    public double getTicketsPurchased() {
        return this.ticketsPurchased;
    }

    public void setJackpotWins(double jackpotWins) {
        this.jackpotWins = jackpotWins;
    }

    public void setJackpotWinnings(long jackpotWinnings) {
        this.jackpotWinnings = jackpotWinnings;
    }

    public void setTicketsPurchased(double ticketsPurchased) {
        this.ticketsPurchased = ticketsPurchased;
    }
}

