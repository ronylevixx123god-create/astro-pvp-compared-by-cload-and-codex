/*
 * Decompiled with CFR 0.150.
 */
package me.recolance.auction.data;

public class BidInformation {
    private String playerName;
    private long bidStart;
    private long maxBid;

    public BidInformation(String playerName, long bidStart, long maxBid) {
        this.playerName = playerName;
        this.bidStart = bidStart;
        this.maxBid = maxBid;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public long getBidStart() {
        return this.bidStart;
    }

    public long getMaxBid() {
        return this.maxBid;
    }
}

