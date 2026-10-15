/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.controller.Controller;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.util.AuctionUtil;
import me.recolance.auction.util.Item;
import me.recolance.auction.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class MenuRenderer {
    private static Plugin plugin = Auction.plugin;
    private static MenuButton pP;
    private static MenuButton rP;
    private static MenuButton nP;
    private static MenuButton hTS;
    private static MenuButton wITP;
    private static String auctionInventoryName;
    private static String itemsSellingName;
    private static String expiredItemName;
    private static List<String> itemsSellingLore;
    private static List<String> expirationLore;

    public static void loadMenuItems() {
        auctionInventoryName = ChatColor.translateAlternateColorCodes((char) '&', (String) plugin.getConfig().getString("menuText.menuName"));
        itemsSellingName = plugin.getConfig().getString("menuText.itemsYoureSelling.name");
        itemsSellingLore = plugin.getConfig().getStringList("menuText.itemsYoureSelling.lore");
        expiredItemName = plugin.getConfig().getString("menuText.collectExpired.name");
        expirationLore = plugin.getConfig().getStringList("menuText.collectExpired.lore");
        String pPN = plugin.getConfig().getString("menuText.previous.name");
        List<String> pPL = plugin.getConfig().getStringList("menuText.previous.lore");
        pP = new MenuButton().type(Material.PAPER).name(pPN);
        for (String lore : pPL) {
            pP.lore(lore);
        }
        String rPN = plugin.getConfig().getString("menuText.refresh.name");
        List<String> rPL = plugin.getConfig().getStringList("menuText.refresh.lore");
        rP = new MenuButton().type(Material.CHEST).name(rPN);
        for (String lore : rPL) {
            rP.lore(lore);
        }
        String nPN = plugin.getConfig().getString("menuText.next.name");
        List<String> nPL = plugin.getConfig().getStringList("menuText.next.lore");
        nP = new MenuButton().type(Material.PAPER).name(nPN);
        for (String lore : nPL) {
            nP.lore(lore);
        }
        String hTSN = plugin.getConfig().getString("menuText.howToSell.name");
        List<String> hTSL = plugin.getConfig().getStringList("menuText.howToSell.lore");
        hTS = new MenuButton().type(Material.GOLD_INGOT).name(hTSN);
        for (String lore : hTSL) {
            hTS.lore(lore);
        }
        hTS.lore("", CC.Gray + "To list an item in a Bid Auction use", CC.Yellow + "/ah startbid <startPrice> <bidIncrement>", "", CC.Gray + "To toggle alerts when you are outbid", CC.Gray + "use " + CC.Yellow + "/ah aucnotify");
        String wITPN = plugin.getConfig().getString("menuText.bookButton.name");
        ArrayList<String> wITPL = Lists.newArrayList("&7This is the auction house, here you can", "&7list items for sale and purchase items", "&7that others have listed for sale.", "", "&7The auction is also a great way to make", "&7money by selling farmable items other", "&7players may be interested in buying.", "", "&7All sell listings last for a max of &e15 minutes", "&7unsold items are sent to your collection bin.", "", "&7You can also list items up for auction", "&7where players can outbid each other to", "&7be the highest bidder using &e/bids&7.", "", "&7For more help, use &e/ah help!");
        wITP = new MenuButton().type(Material.BOOK).name(wITPN);
        for (String lore : wITPL) {
            wITP.lore(lore.replace("15 minute", "60 minute"));
        }
    }

    public static void openAuction(Player player) {
        if (pP == null) {
            MenuRenderer.loadMenuItems();
        }
        Inventory inventory = Bukkit.createInventory(null, (int) 54, (String) auctionInventoryName);
        MenuRenderer.renderAuctionPage(1, inventory, player);
        MenuButton iYS = new MenuButton().type(Material.DIAMOND).name(itemsSellingName);
        for (String string : itemsSellingLore) {
            iYS.lore(string.replace("%selling%", String.valueOf(AuctionUtil.getCurrentlySellingAmount(player))));
        }
        MenuButton cEI = new MenuButton().type(Material.ENDER_CHEST).name(expiredItemName);
        for (String lore : expirationLore) {
            cEI.lore(lore.replace("%expired%", String.valueOf(AuctionUtil.getItemsToCollectAmount(player))));
        }
        inventory.setItem(45, iYS.get());
        inventory.setItem(46, cEI.get());
        BidManager bidManager = Auction.getPlugin().getBidManager();
        List<ItemStack> claimMenu = bidManager.getBidCollectionBin().get(player.getUniqueId());
        if (claimMenu != null && !claimMenu.isEmpty()) {
            inventory.setItem(47, new ItemBuilder(Material.ENDER_CHEST).name(CC.YellowB + "Bid Collection Bin").lore(CC.Gray + "Click to view all items won from bid auctions", CC.Gray + "or expired bids you attempted to sell.", "", CC.YellowB + claimMenu.size() + " Item(s)").build());
        } else {
            inventory.setItem(47, new ItemBuilder(Material.BOOK_AND_QUILL).name(CC.YellowB + "Auction Bids").lore(CC.Gray + "Click here to view all current Auction Biddings!", "", CC.YellowB + bidManager.getActiveAuctionBids().size() + " Auction(s)").build());
        }
        inventory.setItem(48, pP.get());
        inventory.setItem(49, rP.get());
        inventory.setItem(50, nP.get());
        inventory.setItem(51, new ItemBuilder(Material.STORAGE_MINECART, CC.YellowB + "Category View", CC.Gray + "Click to view all current", CC.Gray + "auction item categories!").build());
        inventory.setItem(52, hTS.get());
        inventory.setItem(53, wITP.get());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 2.4f);
        MenuHelper.setViewing(player, MenuType.AUCTION);
    }

    public static void renderAuctionPage(int page, Inventory inventory, Player player) {
        int sI;
        int eI;
        int displaying;
        int lS;
        List<Listing> listings = DataHolder.getListings();
        if (page < 1) {
            lS = listings.size();
            page = lS / 45;
            if (lS % 45 > 0) {
                ++page;
            }
        }
        if ((displaying = (eI = (sI = (page - 1) * 45) + 44 >= (lS = listings.size()) ? lS - 1 : page * 45 - 1) - sI + 1) < 1 && page != 1) {
            MenuRenderer.renderAuctionPage(1, inventory, player);
            return;
        }
        int slot = 0;
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, null);
        }
        ArrayList<Listing> vL = new ArrayList<Listing>();
        for (int i = sI; i <= eI; ++i) {
            Listing listing = listings.get(i);
            inventory.setItem(slot, listing.getMenuDisplayItem());
            ++slot;
            vL.add(listing);
        }
        MenuHelper.setViewingPage(player, page);
        MenuHelper.setViewingListings(player, vL);
    }

    public static void purchaseConfirmation(Player player, Listing listing) {
        MenuRenderer.purchaseConfirmation(player, listing, false);
    }

    public static void purchaseConfirmation(Player player, Listing listing, boolean categoryViewing) {
        Inventory inventory = Bukkit.createInventory(null, (int) 9, (String) "Purchase Item: Are You Sure?");
        ItemStack yes = new MenuButton().type(Material.STAINED_GLASS_PANE).durability(5).name("&a&lConfirm Purchase").lore("&7Click to confirm purchase.").get();
        for (int i = 0; i < 4; ++i) {
            inventory.setItem(i, yes);
        }
        ItemStack no = new MenuButton().type(Material.STAINED_GLASS_PANE).durability(14).name("&c&lCancel Purchase").lore("&7Click to cancel purchase.").get();
        for (int i = 5; i < 9; ++i) {
            inventory.setItem(i, no);
        }
        ItemStack item = new Item(new ItemStack(listing.getItem())).addLore("", "&6Price: &e$" + listing.getPrettyPrice(), "&6Seller: &e" + Bukkit.getOfflinePlayer((UUID) listing.getWhoListed()).getName()).get();
        inventory.setItem(4, item);
        player.openInventory(inventory);
        MenuHelper.setPurchasing(player, listing);
        MenuHelper.setViewing(player, MenuType.PURCHASE_CONFIRMATION);
        if (categoryViewing) {
            player.setMetadata("category_view", (MetadataValue) new FixedMetadataValue((Plugin) Auction.getPlugin(), (Object) ""));
        }
    }

    public static void selling(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int) 54, (String) "Your Current Listings");
        MenuRenderer.renderSelling(player, inventory);
        MenuButton back = new MenuButton().type(Material.CHEST).name("&e&lReturn to Auction House").lore("&7Click here to return to the", "&7auction house main menu.");
        MenuButton wITP = new MenuButton().type(Material.BOOK).name("&e&lTutorial").lore("&7These are your current listings, all of", "&7the items you currently have listed on", "&7the auction house are displayed here.", "", "&7You can cancel and view your listing's", "&7expire time here.");
        inventory.setItem(45, back.get());
        inventory.setItem(53, wITP.get());
        player.openInventory(inventory);
        MenuHelper.setViewing(player, MenuType.SELLING);
    }

    public static void renderSelling(Player player, Inventory inventory) {
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, null);
        }
        List<Listing> playerListings = DataHolder.getPlayerListings().get(player.getUniqueId());
        if (playerListings == null || playerListings.isEmpty()) {
            return;
        }
        if (playerListings.size() > 1) {
            Collections.sort(playerListings, new Comparator<Listing>() {

                @Override
                public int compare(Listing l1, Listing l2) {
                    return Long.valueOf(l1.getEndTime()).compareTo(l2.getEndTime());
                }
            });
        }
        ArrayList<Listing> vL = new ArrayList<Listing>();
        int slot = 0;
        for (Listing listing : playerListings) {
            String perData = listing.getExpInBottle() > 0 ? CC.Gray + " ($" + NumberUtils.formatMoney((double) listing.getExpPerDollar()) + " / xp)" : "";
            double perItem = listing.getPricePerItem();
            if (perItem != (double) listing.getPrice() && perItem > 0.0) {
                perData = " ($" + NumberUtils.formatMoney((double) perItem) + " / item)";
            }
            ItemStack item = new Item(new ItemStack(listing.getItem())).addLore("&7-------------------------", "&c&lClick item to cancel auction.", "", "&6Price: &e$" + listing.getPrettyPrice() + perData, "&6Expire: &e" + Util.getRemainingTimeString(System.currentTimeMillis(), listing.getEndTime() - System.currentTimeMillis()), "&7-------------------------").get();
            vL.add(listing);
            inventory.setItem(slot, item);
            ++slot;
        }
        MenuHelper.setViewingListings(player, vL);
    }

    public static void expired(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int) 54, (String) "Cancelled / Expired Listings");
        MenuRenderer.renderExpired(1, inventory, player);
        MenuButton back = new MenuButton().type(Material.CHEST).name("&e&lReturn to Auction House").lore("&7Click here to return to the", "&7auction house main menu.");
        MenuButton previous = new MenuButton().type(Material.PAPER).name("&e&l<- Previous Page");
        MenuButton all = new MenuButton().type(Material.ENDER_CHEST).name("&e&lClaim All").lore("&7Click here to claim all", "&7your Collection Bin items.");
        MenuButton next = new MenuButton().type(Material.PAPER).name("&e&lNext Page ->");
        MenuButton wITP = new MenuButton().type(Material.BOOK).name("&e&lTutorial").lore("&7This page houses all of your cancelled and", "&7expired items, when a listings is cancelled", "&7or expires you will be able to return that", "&7item back to you from this menu.", "", "&7Just click on the item and if you have enough", "&7inventory space you will recieve the item.");
        inventory.setItem(45, back.get());
        inventory.setItem(48, previous.get());
        inventory.setItem(49, all.get());
        inventory.setItem(50, next.get());
        inventory.setItem(53, wITP.get());
        player.openInventory(inventory);
        MenuHelper.setViewing(player, MenuType.EXPIRED);
    }

    public static void renderExpired(int page, Inventory inventory, Player player) {
        int sI;
        int eI;
        int displaying;
        int lS;
        List<UnclaimedListing> listings = DataHolder.getUnclaimedListings().get(player.getUniqueId());
        if (listings == null) {
            listings = new ArrayList<UnclaimedListing>();
        }
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, null);
        }
        if (page < 1) {
            lS = listings.size();
            page = lS / 45;
            if (lS % 45 > 0) {
                ++page;
            }
        }
        MenuHelper.setViewingPage(player, page);
        if (listings.isEmpty()) {
            return;
        }
        if (listings.size() > 1) {
            Collections.sort(listings, new Comparator<UnclaimedListing>() {

                @Override
                public int compare(UnclaimedListing l1, UnclaimedListing l2) {
                    return Long.valueOf(l1.getExpireTime()).compareTo(l2.getExpireTime());
                }
            });
            Collections.reverse(listings);
        }
        if ((displaying = (eI = (sI = (page - 1) * 45) + 44 >= (lS = listings.size()) ? lS - 1 : page * 45 - 1) - sI + 1) < 1 && page != 1) {
            MenuRenderer.renderExpired(1, inventory, player);
            return;
        }
        ArrayList<UnclaimedListing> vL = new ArrayList<UnclaimedListing>();
        ArrayList<UnclaimedListing> rL = new ArrayList<UnclaimedListing>();
        int slot = 0;
        for (int i = sI; i <= eI; ++i) {
            UnclaimedListing listing = listings.get(i);
            if (listing.getExpireTime() < System.currentTimeMillis()) {
                rL.add(listing);
                continue;
            }
            ItemStack item = new Item(new ItemStack(listing.getItem())).addLore("&7-------------------------", "&e&lClick here to claim item.", "", "&6Item Deletion: &e" + Util.getRemainingTimeString(System.currentTimeMillis(), listing.getExpireTime() - System.currentTimeMillis()), "&7-------------------------").get();
            inventory.setItem(slot, item);
            ++slot;
            vL.add(listing);
        }
        for (UnclaimedListing l : rL) {
            Controller.expireUnclaimed(l);
        }
        MenuHelper.setViewingUnclaimed(player, vL);
    }
}

