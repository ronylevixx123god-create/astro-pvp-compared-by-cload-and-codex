/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.recolance.auction.util;

import me.recolance.auction.Auction;
import me.recolance.auction.data.DataHolder;
import org.bukkit.entity.Player;

public class AuctionUtil {
    public static int getCurrentlySellingAmount(Player player) {
        if (!DataHolder.getPlayerListings().containsKey(player.getUniqueId())) {
            return 0;
        }
        return DataHolder.getPlayerListings().get(player.getUniqueId()).size();
    }

    public static int getItemsToCollectAmount(Player player) {
        if (!DataHolder.getUnclaimedListings().containsKey(player.getUniqueId())) {
            return 0;
        }
        return DataHolder.getUnclaimedListings().get(player.getUniqueId()).size();
    }

    public static long getEndTime() {
        return System.currentTimeMillis() + 864000000; //10 days
    }

    public static long getUnclaimedEndTime() {
        return System.currentTimeMillis() + (long) (Auction.plugin.getConfig().getInt("auction.unclaimedHours") * 3600000);
    }

    public static boolean canSellItemPermission(Player player) {
        int currentlySelling = AuctionUtil.getCurrentlySellingAmount(player);
        if (player.isOp()) {
            return true;
        }
        int limit = AuctionUtil.getSellItemLimit(player);
        return currentlySelling + 1 <= limit;
    }

    private static int getSellItemLimit(Player p) {
        if (p.hasPermission("auction.limit.8")) {
            return 8;
        }
        if (p.hasPermission("auction.limit.6")) {
            return 6;
        }
        if (p.hasPermission("auction.limit.5")) {
            return 5;
        }
        if (p.hasPermission("auction.limit.4")) {
            return 4;
        }
        if (p.hasPermission("auction.limit.3")) {
            return 3;
        }
        if (p.hasPermission("auction.limit.2")) {
            return 2;
        }
        return 1;
    }
}

