/*
 * Decompiled with CFR 0.150.
 */
package me.recolance.auction.command;

public enum Command {
    AUCTION("&6Auction Menu", "&e/auction", "&7Opens the auction house.", "", "&9Aliases:", "&e- /ah", "&e- /auc", "&e- /auctionhouse"),
    SELL("&6Sell An Item", "&e/ah sell <price>", "&7List the item you are holding on the auction house.", "", ""),
    EXPIRED("&6Open Your Expired Items", "&e/ah expired", "&7View and manage your cancelled and expired items.", "", "&9Aliases:", "&e- /ah ex"),
    SELLING("&6Open Your Listed Items", "&e/ah listed", "&7View and manage the items you are selling.", "", "&9Aliases:", "&e- /ah selling"),
    HELP("&6Auction Help", "&e/ah help", "&7View this help menu.", "", "&9Aliases:", "&e- /ah commands");

    private String[] desc;

    private Command(String... desc) {
        this.desc = desc;
    }

    public String[] getDescription() {
        return this.desc;
    }
}

