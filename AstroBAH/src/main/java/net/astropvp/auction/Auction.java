//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.auction;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.astropvp.util.Utils;
import org.bukkit.entity.Player;
import org.apache.commons.lang.WordUtils;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.data.DataHandler;
import me.recolance.auction.auction.UnclaimedListing;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import com.cosmicpvp.cosmicutils.utils.CC;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public class Auction
{
    public ItemStack item;
    public long endsAt;
    public double bidIncrement;
    public double currentBid;
    public UUID currentTopBidder;
    public UUID auctionUUID;
    public String topBidderName;
    public static HashMap<Double, Double> incrementThreshold;

    public Auction(final ItemStack item, final int bidIncrement) {
        this.item = item;
        this.endsAt = System.currentTimeMillis() + 300000L;
        this.bidIncrement = bidIncrement;
        this.currentBid = 0.0;
        this.currentTopBidder = null;
        this.auctionUUID = UUID.randomUUID();
    }

    public void onEnd() {
        if (AuctionManager.get().getCurrentAuction() != this || AuctionManager.get().getJournal().isPending()) return;
        AuctionManager.get().getJournal().save(this, "reward-pending", null, 0);
        if (this.currentTopBidder != null) {
            Utils.sendCenteredMessage("");
            Bukkit.broadcastMessage(CC.translate("&a&l[/bah] &f&l" + this.topBidderName + " &ahas won the bid on " + this.item.getItemMeta().getDisplayName() + "&a for &a&l$&a" + Utils.formatMoney(this.currentBid) + "!"));
            Utils.sendCenteredMessage("");
            UnclaimedListing reward = new UnclaimedListing(this.auctionUUID, this.currentTopBidder,
                    this.item.clone(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1L));
            // The historical implementation only updated memory, losing rewards on restart.
            DataHandler.saveUnclaimedListing(reward, false);
            AuctionManager.get().getJournal().confirmRewardPersistence(reward);
            DataHolder.addUnclaimedListing(this.currentTopBidder, reward);
        }
        else {
            String displayName = this.item.getItemMeta().getDisplayName();
            if (displayName == null) {
                displayName = WordUtils.capitalizeFully(this.item.getType().name().replace("_", " "));
            }
            Utils.sendCenteredMessage("");
            Bukkit.broadcastMessage(CC.translate("&b&l[/bah] No players &bhave won the bid on " + displayName + "&b!"));
            Utils.sendCenteredMessage("");
        }
        AuctionManager.get().setCurrentAuction(null);
        AuctionManager.get().addPastAuction(new PastAuction(topBidderName, currentBid,
                Utils.toBase64(new ItemStack[]{item}, 9), System.currentTimeMillis()));
        AuctionManager.get().getJournal().clear();
    }

    public void doBid(final Player player, final double bidAmount) {
        if (AuctionManager.get().getCurrentAuction() != this || endsAt <= System.currentTimeMillis()) {
            player.sendMessage(CC.translate("&cThis auction has already ended."));
            return;
        }
        if (player.getUniqueId().equals(currentTopBidder)) {
            player.sendMessage(CC.translate("&cYou are already the highest bidder."));
            return;
        }
        if (!Double.isFinite(bidAmount) || bidAmount < currentBid + bidIncrement) {
            player.sendMessage(CC.translate("&cYour bid must meet the current minimum increment."));
            return;
        }
        AuctionJournal journal = AuctionManager.get().getJournal();
        if (journal.isPending()) { player.sendMessage(CC.translate("&cAuction escrow requires administrator reconciliation.")); return; }
        Economy economy = BlackMarketAuction.get().getEconomy();
        if (!economy.has(player, bidAmount)) {
            player.sendMessage(CC.translate("&c&l[/bah] &cYou do not have enough money to bid on this item."));
            return;
        }
        if (bidAmount <= this.currentBid) {
            player.sendMessage(CC.translate("&c&l(!) &cYour bid has already been outbid!"));
            player.sendMessage(CC.translate("&cYour bid: &7$" + Utils.formatMoney(bidAmount)));
            player.sendMessage(CC.translate("&cCurrent bid: &7$" + Utils.formatMoney(AuctionManager.get().getCurrentAuction().currentBid)));
            return;
        }
        UUID previousHolder = currentTopBidder;
        journal.flushEconomy(player.getUniqueId());
        journal.flushEconomy(previousHolder);
        journal.save(this, "bid-pending", player.getUniqueId(), bidAmount);
        EconomyResponse withdrawal = economy.withdrawPlayer(player, bidAmount);
        if (!withdrawal.transactionSuccess()) {
            journal.save(this, "active", null, 0);
            player.sendMessage(CC.translate("&cUnable to withdraw your bid. Please try again."));
            return;
        }
        if (currentTopBidder != null) {
            EconomyResponse refund = economy.depositPlayer(Bukkit.getOfflinePlayer(currentTopBidder), currentBid);
            if (!refund.transactionSuccess()) {
                EconomyResponse rollback = economy.depositPlayer(player, bidAmount);
                if (!rollback.transactionSuccess()) {
                    BlackMarketAuction.get().getLogger().severe("Bid rollback failed for "
                            + player.getUniqueId() + " amount " + bidAmount);
                }
                if (rollback.transactionSuccess()) {
                    journal.flushEconomy(player.getUniqueId());
                    journal.save(this, "active", null, 0);
                }
                player.sendMessage(CC.translate("&cUnable to refund the previous bidder; bid cancelled."));
                return;
            }
        }
        if (this.endsAt - System.currentTimeMillis() <= 10000L) {
            Bukkit.broadcastMessage(CC.translate("&c&l[/bah] &cA bid of &c&l$&c" + Utils.formatMoney(bidAmount) + " has been made at the last moment!"));
            Bukkit.broadcastMessage(CC.translate("&7The auction has been extended by 10 seconds."));
            this.endsAt += 10000L;
        }
        if (this.currentTopBidder != null) {
            Player previousBidder = Bukkit.getPlayer(this.currentTopBidder);
            if (previousBidder != null) previousBidder.sendMessage(CC.translate("&c&l[/bah] &cYou have been outbid by " + player.getName() + " for " + this.item.getItemMeta().getDisplayName()));
        }
        this.currentBid = bidAmount;
        this.currentTopBidder = player.getUniqueId();
        this.topBidderName = player.getName();
        if (Auction.incrementThreshold.containsKey(this.currentBid)) {
            this.bidIncrement = Auction.incrementThreshold.get(this.currentBid);
            Bukkit.broadcastMessage(CC.translate("&b&l[/bah] &b" + player.getName() + " has bid &b&l$&b" + Utils.formatMoney(bidAmount) + " on " + this.item.getItemMeta().getDisplayName() + "&b, the bid increment is now &b&l$&b" + Utils.formatMoney(this.bidIncrement) + "&b!"));
        }
        String displayName = this.item.getItemMeta().getDisplayName();
        if (displayName == null) {
            displayName = WordUtils.capitalizeFully(this.item.getType().name().replace("_", " "));
        }
        journal.flushEconomy(player.getUniqueId());
        journal.flushEconomy(previousHolder);
        journal.save(this, "active", null, 0);
        player.sendMessage(CC.translate("&b&l[/bah] &bYou have bid &b&l$&b" + Utils.formatMoney(bidAmount) + "&b on " + displayName));
    }

    static {
        Auction.incrementThreshold = new HashMap<Double, Double>() {
            {
                this.put(1000000.0, 500000.0);
                this.put(5000000.0, 1000000.0);
                this.put(1.0E7, 2500000.0);
                this.put(2.5E7, 5000000.0);
                this.put(1.0E8, 1.0E7);
                this.put(5.0E8, 5.0E7);
                this.put(1.0E9, 2.0E8);
                this.put(5.0E9, 1.0E9);
            }
        };
    }
}
