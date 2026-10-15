/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu;

import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuHelper {
    public static HashMap<Player, Integer> viewingPage = new HashMap();
    public static HashMap<Player, List<Listing>> viewingListings = new HashMap();
    public static HashMap<Player, Listing> purchasing = new HashMap();
    public static HashMap<Player, List<UnclaimedListing>> viewingUnclaimed = new HashMap();
    private static HashMap<UUID, MenuType> currentlyViewing = new HashMap();

    public static boolean isViewing(Player player) {
        return currentlyViewing.containsKey(player.getUniqueId());
    }

    public static MenuType getViewing(Player player) {
        return currentlyViewing.get(player.getUniqueId());
    }

    public static void setViewing(Player player, MenuType menuType) {
        currentlyViewing.put(player.getUniqueId(), menuType);
    }

    public static void removeViewing(Player player) {
        currentlyViewing.remove(player.getUniqueId());
    }

    public static int getViewingPage(Player player) {
        if (!viewingPage.containsKey((Object) player)) {
            player.closeInventory();
            return -1;
        }
        return viewingPage.get((Object) player);
    }

    public static void setViewingPage(Player player, int page) {
        viewingPage.put(player, page);
    }

    public static void removeViewingPage(Player player) {
        viewingPage.remove((Object) player);
    }

    public static List<Listing> getViewingListings(Player player) {
        return viewingListings.get((Object) player);
    }

    public static void setViewingListings(Player player, List<Listing> list) {
        viewingListings.put(player, list);
    }

    public static void removeViewingListings(Player player) {
        viewingListings.remove((Object) player);
    }

    public static Listing getPurchasing(Player player) {
        return purchasing.get((Object) player);
    }

    public static void setPurchasing(Player player, Listing list) {
        purchasing.put(player, list);
    }

    public static void removePurchasing(Player player) {
        purchasing.remove((Object) player);
        player.removeMetadata("category_view", (Plugin) Auction.getPlugin());
    }

    public static List<UnclaimedListing> getUnclaimedViewing(Player player) {
        return viewingUnclaimed.get((Object) player);
    }

    public static void setViewingUnclaimed(Player player, List<UnclaimedListing> list) {
        viewingUnclaimed.put(player, list);
    }

    public static void removeViewingUnclaimed(Player player) {
        viewingUnclaimed.remove((Object) player);
    }
}

