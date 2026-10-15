/*
 * Decompiled with CFR 0.150.
 */
package me.recolance.auction.data;

import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataHolder {
    private static List<Listing> listings = new ArrayList<Listing>();
    private static HashMap<UUID, List<Listing>> playerListings = new HashMap();
    private static HashMap<UUID, List<UnclaimedListing>> unclaimedListings = new HashMap();

    public static List<Listing> getListings() {
        return listings;
    }

    public static void setListings(List<Listing> list) {
        listings = list;
    }

    public static void addListing(Listing listing) {
        DataHolder.getListings().add(0, listing);
    }

    public static void removeListing(Listing listing) {
        listing.setValid(false);
        listings.remove(listing);
    }

    public static HashMap<UUID, List<Listing>> getPlayerListings() {
        return playerListings;
    }

    public static void setPlayerListings(HashMap<UUID, List<Listing>> list) {
        playerListings = list;
    }

    public static void addPlayerListing(UUID playerId, Listing listing) {
        if (!DataHolder.getPlayerListings().containsKey(playerId)) {
            ArrayList<Listing> listings = new ArrayList<Listing>();
            listings.add(listing);
            DataHolder.getPlayerListings().put(playerId, listings);
        } else {
            playerListings.get(playerId).add(listing);
        }
    }

    public static void removePlayerListing(UUID playerId, Listing listing) {
        if (!DataHolder.getPlayerListings().containsKey(playerId)) {
            return;
        }
        DataHolder.getPlayerListings().get(playerId).remove(listing);
    }

    public static HashMap<UUID, List<UnclaimedListing>> getUnclaimedListings() {
        return unclaimedListings;
    }

    public static void setUnclaimedListings(HashMap<UUID, List<UnclaimedListing>> uList) {
        unclaimedListings = uList;
    }

    public static void addUnclaimedListing(UUID playerId, UnclaimedListing listing) {
        if (!DataHolder.getUnclaimedListings().containsKey(playerId)) {
            ArrayList<UnclaimedListing> listings = new ArrayList<UnclaimedListing>();
            listings.add(listing);
            DataHolder.getUnclaimedListings().put(playerId, listings);
        } else {
            DataHolder.getUnclaimedListings().get(playerId).add(listing);
        }
    }

    public static void removeUnclaimedListing(UUID playerId, UnclaimedListing listing) {
        if (!DataHolder.getUnclaimedListings().containsKey(playerId)) {
            return;
        }
        DataHolder.getUnclaimedListings().get(playerId).remove(listing);
    }

    public static void removeUnclaimedListingStore(UUID playerId) {
        DataHolder.getUnclaimedListings().remove(playerId);
    }
}

