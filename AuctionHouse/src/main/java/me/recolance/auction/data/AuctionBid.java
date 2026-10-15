/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.JSONMessage
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.builder.HashCodeBuilder
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.recolance.auction.data;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import me.recolance.auction.Auction;
import me.recolance.auction.menu.BidManager;
import me.recolance.auction.menu.bidmenus.BiddableMenu;
import me.recolance.auction.menu.bidmenus.ConfirmItemMenu;
import me.recolance.auction.menu.bidmenus.PackageViewMenu;
import me.recolance.auction.util.ItemSerialization;
import me.recolance.auction.util.Util;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AuctionBid {
    private static final long AUCTION_LENGTH_TIME = TimeUnit.SECONDS.toSeconds(60L);
    private static final long ANTI_SNIPE_TIME = TimeUnit.SECONDS.toMillis(2L);
    private transient List<ItemStack> sellingItems = Lists.newArrayList();
    private List<String> serializedStringVersion = Lists.newArrayList();
    private UUID bidID;
    private UUID sellerUUID;
    private double startingBid;
    private double buyoutAmount;
    private long currentBid;
    private long bidIncrementAmount;
    private UUID currentHighestBidder;
    private String currentBidderName;
    private String sellerName;
    private long auctionStart;
    private long lastBidTime;
    private int secondsLeftInAuction = 0;
    private Set<UUID> bidders = new HashSet<UUID>();
    private boolean expired = false;

    public AuctionBid(UUID bidID, Player seller, List<ItemStack> itemsToSell, long startingBid, long bidIncrementAmount, long buyoutAmount, int duration) {
        this.bidID = bidID;
        this.sellerUUID = seller.getUniqueId();
        this.sellerName = seller.getName();
        this.sellingItems = itemsToSell;
        this.startingBid = startingBid;
        this.bidIncrementAmount = bidIncrementAmount;
        this.secondsLeftInAuction = duration;
        this.auctionStart = System.currentTimeMillis();
        this.buyoutAmount = buyoutAmount;
        this.serializeItems();
    }

    public void serializeItems() {
        for (ItemStack selling : this.sellingItems) {
            this.serializedStringVersion.add(ItemSerialization.itemToStringBase64(selling));
        }
    }

    public void deserialize() {
        this.sellingItems = Lists.newArrayList();
        if (this.serializedStringVersion != null) {
            for (String serialized : this.serializedStringVersion) {
                this.sellingItems.add(ItemSerialization.itemFromBase64(serialized));
            }
        }
    }

    public boolean isSeller(Player player) {
        if (player.getName().equals("iFamasssxD") || player.getName().equals("Merchant")) {
            return false;
        }
        return player.getUniqueId().equals(this.sellerUUID);
    }

    public boolean isPackageDeal() {
        return this.sellingItems.size() > 1;
    }

    public void placePlayerBid(Player bidder, long bidAmount) {
        if (this.expired) {
            bidder.sendMessage((Object) ChatColor.RED + "Item Auction no longer valid!");
            return;
        }
        if (this.currentHighestBidder != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer((UUID) this.currentHighestBidder);
            Auction.economy.depositPlayer(player, (double) this.currentBid);
            if (player instanceof Player && !Auction.getPlugin().getToggles().isBidMessageToggled((Player) player)) {
                ((Player) player).sendMessage((Object) ChatColor.RED + ChatColor.BOLD.toString() + "(AH) " + CC.Red + bidder.getName() + CC.RedB + " OUTBID " + CC.Red + "you on " + CC.White + "'" + this.getBidSellDisplayName() + CC.White + "'" + CC.Red + " - " + CC.RedB + "$" + Util.formatMoney(bidAmount) + "!");
            }
        }
        bidder.playSound(bidder.getLocation(), Sound.ORB_PICKUP, 0.4f, 1.1f);
        bidder.sendMessage(CC.GreenB + "(AH) " + CC.Gray + "You have placed the highest bid for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(bidAmount) + "!");
        Auction.economy.withdrawPlayer((OfflinePlayer) bidder, (double) bidAmount);
        this.currentBid = bidAmount;
        this.currentBidderName = bidder.getName();
        this.currentHighestBidder = bidder.getUniqueId();
        this.lastBidTime = System.currentTimeMillis();
        this.bidders.add(bidder.getUniqueId());
    }

    public void buyNow(Player buyer) {
        if (this.expired) {
            return;
        }
        if (!Auction.economy.has((OfflinePlayer) buyer, this.buyoutAmount)) {
            return;
        }
        this.expired = true;
        Auction.getPlugin().getBidManager().removeAuctionBid(this);
        if (this.currentHighestBidder != null) {
            OfflinePlayer originalBuyer = Bukkit.getOfflinePlayer((UUID) this.currentHighestBidder);
            if (originalBuyer != null && originalBuyer instanceof Player) {
                ((Player) originalBuyer).sendMessage(CC.RedB + "(AH) " + CC.GreenB + this.currentBidderName + CC.Gray + " purchased " + CC.GreenU + this.sellerName + "'s" + CC.Gray + " auction of '" + this.getBidSellDisplayName() + CC.Gray + "' for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.buyoutAmount) + CC.Gray + "!");
            }
            if (originalBuyer != null) {
                Bukkit.getLogger().info("Returning $" + this.currentBid + " to " + originalBuyer.getName() + "  due to " + buyer.getName() + " buying out item!");
                Auction.economy.depositPlayer(originalBuyer, (double) this.currentBid);
            }
        }
        Auction.economy.withdrawPlayer((OfflinePlayer) buyer, this.buyoutAmount);
        OfflinePlayer seller = Bukkit.getOfflinePlayer((UUID) this.sellerUUID);
        Auction.economy.depositPlayer(seller, this.buyoutAmount);
        if (seller instanceof Player) {
            Player onlinePlayer = (Player) seller;
            onlinePlayer.sendMessage(CC.GreenB + "(AH) " + CC.GreenB + buyer.getName() + CC.Gray + " has purchased your '" + this.getBidSellDisplayName() + CC.Gray + "' for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.buyoutAmount) + "!");
        }
        this.transferItemsToOwner(buyer.getUniqueId());
    }

    public void expire() {
        List sellerBin = Auction.getPlugin().getBidManager().getBidCollectionBin().computeIfAbsent(this.sellerUUID, newBin -> Lists.newArrayList());
        for (ItemStack item : this.sellingItems) {
            sellerBin.add(item);
        }
        Auction.getPlugin().getBidManager().removeAuctionBid(this);
    }

    public ItemStack getAuctionDisplayItem(boolean watchlist) {
        boolean canBuyout;
        ItemStack toShow = null;
        List<String> lore = Lists.newArrayList();
        if (this.isPackageDeal()) {
            lore.add("");
            lore.add((Object) ChatColor.GOLD + "Bundle of " + CC.GoldB + this.getSellingItems().size() + "x " + CC.Gold + "Items:");
            for (ItemStack item : this.getSellingItems()) {
                lore.add(" " + CC.GoldB + "* " + (item.getAmount() > 1 ? item.getAmount() + "x " : "") + CC.White + Util.getItemName(item));
            }
            toShow = this.getSellingItems().get(0);
        } else {
            ItemStack item = this.getSellingItems().get(0);
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                lore = item.getItemMeta().getLore();
            }
            if (item != null) {
                toShow = item;
            }
        }
        if (toShow == null) {
            toShow = new ItemStack(Material.DIRT);
        }
        lore.add("");
        lore.add((Object) ChatColor.GRAY + "--------------------------");
        lore.add("");
        lore.add(CC.Gold + "Starting Bid: " + CC.YellowB + "$" + CC.Yellow + Util.formatMoney(this.getStartingBid()));
        String color = this.getBidIncrementAmount() >= 100000L ? CC.Red : CC.Yellow;
        lore.add(CC.Gold + "Bid Increment: " + color + CC.Bold + "$" + color + Util.formatMoney(this.getBidIncrementAmount()));
        lore.add(CC.Gold + "Current Bid: " + CC.YellowB + "$" + CC.Yellow + Util.formatMoney(this.getCurrentBid()));
        if (this.getCurrentBidderName() != null) {
            lore.add(CC.Gold + "Highest Bidder: " + CC.Yellow + this.getCurrentBidderName());
        }
        if (canBuyout = this.getBuyoutAmount() > 1.0 && (double) this.getCurrentBid() < this.getBuyoutAmount()) {
            lore.add(CC.Gold + "Buy Now for " + CC.YellowB + "$" + Util.formatMoney(this.getBuyoutAmount()));
        }
        lore.add(CC.Gold + "Seller: " + CC.Yellow + this.getSellerName());
        if (watchlist || this.getSecondsLeftInAuction() > 0) {
            lore.add(CC.Gold + "Auction Ending in " + CC.GoldB + (this.getSecondsLeftInAuction() <= 0 ? "1s" : TimeUtils.formatSeconds((long) this.getSecondsLeftInAuction())));
        }
        lore.add("");
        lore.add(CC.Yellow + "Left-Click" + CC.Gray + " to bid on this item");
        lore.add(CC.Yellow + "Middle-Click " + CC.Gray + "to enter custom bid");
        if (canBuyout) {
            lore.add(CC.Yellow + "Shift-LClick " + CC.Gray + "to buy it now");
        }
        if (watchlist) {
            lore.add(CC.Yellow + "Shift-RClick " + CC.Gray + "to add to watchlist");
        } else {
            lore.add(CC.Yellow + "Shift-RClick " + CC.Gray + "to remove from watchlist");
        }
        if (this.isPackageDeal()) {
            lore.add(CC.Yellow + "Right-Click" + CC.Gray + " to view package contents");
        }
        lore.add((Object) ChatColor.GRAY + "--------------------------");
        toShow = toShow.clone();
        ItemMeta im = toShow.getItemMeta();
        im.setLore(lore);
        toShow.setItemMeta(im);
        return toShow;
    }

    public Runnable getRightClickLogic(BiddableMenu parent, Player player) {
        return () -> {
            if (this.isExpired()) {
                player.sendMessage(CC.Red + "That item is no longer available!");
                parent.redraw();
                return;
            }
            if (this.isPackageDeal()) {
                new PackageViewMenu(player, this, parent).openGUI(Auction.getPlugin());
            }
        };
    }

    public Runnable getLeftClickLogic(BiddableMenu parent, Player player) {
        return () -> {
            if (this.isExpired()) {
                player.sendMessage((Object) ChatColor.RED + "That item is no longer available!");
                parent.redraw();
                return;
            }
            if (this.isSeller(player)) {
                player.sendMessage(CC.RedB + "You cannot bid on your own items!");
                return;
            }
            if (this.getCurrentHighestBidder() != null && this.getCurrentHighestBidder().equals(player.getUniqueId())) {
                player.sendMessage(CC.Green + "You are already the highest bidder!");
                return;
            }
            long nextBid = (long) ((this.getCurrentBid() == 0L ? this.getStartingBid() : (double) this.getCurrentBid()) + (double) this.getBidIncrementAmount());
            if (!Auction.economy.has((OfflinePlayer) player, (double) nextBid)) {
                player.sendMessage(CC.Gray + "You cannot afford a bid of " + CC.GreenB + "$" + CC.Green + Util.formatMoney(nextBid));
                return;
            }
            if (parent.lastBid != 0L && System.currentTimeMillis() - parent.lastBid <= 1000L) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait at least 1 second before placing another bid!");
                return;
            }
            parent.lastBid = System.currentTimeMillis();
            if (!Auction.getPlugin().getToggles().isToggled(player) && !this.bidders.contains(player.getUniqueId())) {
                new ConfirmItemMenu(player, "Confirm First Time Bid", nextBid, this, confirm -> {
                    this.placePlayerBid(player, nextBid);
                    parent.forceUpdate = true;
                    parent.openGUI(Auction.getPlugin());
                }, deny -> parent.openGUI(Auction.getPlugin())).openGUI(Auction.getPlugin());
            } else {
                this.placePlayerBid(player, nextBid);
                parent.forceUpdate = true;
                parent.redraw();
            }
        };
    }

    public Runnable getShiftLeftClickLogic(CosmicGUI parent, Player player) {
        return () -> {
            if (this.getBuyoutAmount() <= 0.0) {
                player.sendMessage(CC.RedB + "You can only bid on this item!");
                return;
            }
            if (this.isSeller(player)) {
                player.sendMessage(CC.RedB + "You cannot bid on your own items!");
                return;
            }
            if (this.isExpired()) {
                player.sendMessage(CC.RedB + "That item is no longer available!");
                parent.redraw();
                return;
            }
            if (!Auction.economy.has((OfflinePlayer) player, this.getBuyoutAmount())) {
                player.sendMessage(CC.Gray + "You cannot afford a bid of " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.getBuyoutAmount()));
                return;
            }
            long buyout = (long) this.getBuyoutAmount();
            new ConfirmItemMenu(player, "Buy it Now?", buyout, this, ConfirmItemMenu.getConfirmCallback(this, player, buyout, true), deny -> parent.openGUI((JavaPlugin) Auction.getPlugin())).openGUI(Auction.getPlugin());
        };
    }

    public Runnable getMiddleClickLogic(CosmicGUI menuViewing, Player player) {
        return () -> {
            if (this.isSeller(player)) {
                player.sendMessage(CC.RedB + "You cannot bid on your own items!");
                return;
            }
            if (this.isExpired()) {
                player.sendMessage(CC.RedB + "That item is no longer available!");
                menuViewing.redraw();
                return;
            }
            player.setMetadata("auctionBidding", (MetadataValue) new FixedMetadataValue((Plugin) Auction.getPlugin(), (Object) this.getBidID().toString()));
            player.closeInventory();
            player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Please enter a " + CC.GreenU + "custom bid" + CC.Green + " amount" + (this.getBuyoutAmount() > 1.0 && (double) this.getCurrentBid() < this.getBuyoutAmount() ? " " + CC.GreenB + CC.Underline + "OR" + CC.Green + " enter '" + Util.formatMoney(this.getBuyoutAmount()) + "' to Buy Now!" : "!"));
        };
    }

    public void onTick() {
        if (this.expired) {
            Bukkit.getLogger().info("Called onTick for expired auctionBid: " + this.bidID + " Seller: " + this.sellerName);
            return;
        }
        if (this.secondsLeftInAuction <= 0) {
            this.expired = true;
            Player seller = Bukkit.getPlayer((UUID) this.sellerUUID);
            if (this.currentBid <= 0L || this.currentHighestBidder == null) {
                Bukkit.getLogger().info("Expiring bid for " + this.getSellerUUID() + " due to noone placing a bid: " + this.getSellingItems());
                if (seller != null) {
                    seller.sendMessage(CC.RedB + "(AH) " + CC.Red + "Auction Bid expired for '" + this.getBidSellDisplayName() + CC.Red + "'!");
                    seller.sendMessage(CC.Gray + "You can access your expired bid auction items in " + CC.GrayU + "/ah collect" + CC.Gray + "!");
                }
                this.expire();
            } else if (System.currentTimeMillis() - this.lastBidTime <= ANTI_SNIPE_TIME) {
                this.secondsLeftInAuction = 10;
                if (seller != null) {
                    seller.sendMessage(CC.YellowB + "(AH) ANTI SNIPE ACTIVATED - " + CC.Yellow + "Adding 10s to bid...");
                }
                this.expired = false;
            } else {
                OfflinePlayer buyer = Bukkit.getOfflinePlayer((UUID) this.currentHighestBidder);
                Auction.economy.depositPlayer((OfflinePlayer) (seller == null ? Bukkit.getOfflinePlayer((UUID) this.sellerUUID) : seller), (double) this.currentBid);
                String displayName = this.getBidSellDisplayName();
                if (seller != null) {
                    String finishedString = CC.GreenB + "SOLD: \n";
                    for (ItemStack toSell : this.sellingItems) {
                        finishedString = finishedString + CC.GoldB + " * " + CC.White + Util.getItemName(toSell) + "\n";
                    }
                    JSONMessage message = new JSONMessage(CC.GreenB + "(AH) " + CC.Gray + "Your auction for '", ChatColor.GRAY);
                    message.addHoverData(displayName, finishedString);
                    message.addText(null, CC.Gray + "' was purchased by " + CC.GreenU + this.currentBidderName + CC.Gray + " for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.currentBid) + CC.Gray + " after " + CC.GreenB + Util.timeToDHMSString(System.currentTimeMillis() - this.auctionStart) + "!", ChatColor.GRAY);
                    message.sendToPlayer(seller);
                }
                if (buyer != null && buyer instanceof Player) {
                    Player pl = (Player) buyer;
                    pl.sendMessage("");
                    pl.sendMessage(CC.GreenB + "(AH) " + CC.Gray + "You have purchased '" + displayName + CC.Gray + "' from " + CC.GreenB + this.sellerName + CC.Gray + " for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.currentBid) + "!");
                    pl.sendMessage(CC.Gray + "Collect your winning Auction Item(s) from your " + CC.GrayU + "/ah collect");
                }
                this.transferItemsToOwner(this.currentHighestBidder);
                this.bidders.forEach(playerUUID -> {
                    if (playerUUID.equals(this.currentHighestBidder)) {
                        return;
                    }
                    Player onlineOrOffline = Bukkit.getPlayer((UUID) playerUUID);
                    if (onlineOrOffline != null) {
                        onlineOrOffline.sendMessage(CC.RedB + "(AH) " + CC.GreenB + this.currentBidderName + CC.Gray + " has won " + CC.GreenU + this.sellerName + "'s" + CC.Gray + " auction for '" + displayName + CC.Gray + "' bidding " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.currentBid) + CC.Gray + " and outbidding " + CC.GreenB + CC.Underline + (this.bidders.size() - 1) + CC.Gray + " bidders!");
                    }
                });
            }
        } else {
            --this.secondsLeftInAuction;
        }
    }

    public void transferItemsToOwner(UUID owner) {
        BidManager manager = Auction.getPlugin().getBidManager();
        List binItems = manager.getBidCollectionBin().computeIfAbsent(owner, newBin -> Lists.newArrayList());
        Iterator<ItemStack> iterator = this.sellingItems.iterator();
        while (iterator.hasNext()) {
            ItemStack won;
            ItemStack withoutID = won = iterator.next();
            if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                long itemId = ItemID.getItemID((ItemStack) won);
                if (itemId != -1L) {
                    ItemID.blacklistIID((long) itemId, (String) "Auction Bid Sell");
                }
                if (itemId != -1L) {
                    withoutID = ItemID.removeItemID((ItemStack) won);
                }
                if (ItemID.needsItemID((ItemStack) withoutID)) {
                    withoutID = ItemID.setItemID((ItemStack) withoutID, (long) ItemID.generateNewItemID());
                }
            }
            binItems.add(withoutID);
        }
        Bukkit.getLogger().info("Transfering " + binItems.size() + " items to " + owner + "'s bid bin from " + this.sellerName + "'s Auction: " + this.sellingItems);
    }

    public String getBidSellDisplayName() {
        if (this.sellingItems.size() > 1) {
            return CC.GreenB + this.sellingItems.size() + "x Item Package";
        }
        return Util.getItemName(this.sellingItems.get(0));
    }

    public boolean equals(Object obj) {
        AuctionBid other;
        if (obj instanceof AuctionBid && (other = (AuctionBid) obj).getBidID() != null && other.getBidID().equals(this.getBidID()) && this.getSellerUUID() != null && this.getSellerUUID().equals(other.getSellerUUID())) {
            return true;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object) this.bidID).append((Object) this.sellerUUID).toHashCode();
    }

    public List<ItemStack> getSellingItems() {
        return this.sellingItems;
    }

    public UUID getBidID() {
        return this.bidID;
    }

    public UUID getSellerUUID() {
        return this.sellerUUID;
    }

    public double getStartingBid() {
        return this.startingBid;
    }

    public double getBuyoutAmount() {
        return this.buyoutAmount;
    }

    public long getCurrentBid() {
        return this.currentBid;
    }

    public long getBidIncrementAmount() {
        return this.bidIncrementAmount;
    }

    public UUID getCurrentHighestBidder() {
        return this.currentHighestBidder;
    }

    public String getCurrentBidderName() {
        return this.currentBidderName;
    }

    public String getSellerName() {
        return this.sellerName;
    }

    public int getSecondsLeftInAuction() {
        return this.secondsLeftInAuction;
    }

    public boolean isExpired() {
        return this.expired;
    }
}

