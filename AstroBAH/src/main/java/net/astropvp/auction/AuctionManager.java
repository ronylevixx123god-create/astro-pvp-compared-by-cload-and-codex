package net.astropvp.auction;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.astropvp.util.ConfigUtils;
import net.astropvp.util.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import org.bukkit.Bukkit;
import net.milkbowl.vault.economy.EconomyResponse;

public class AuctionManager
{
    private static AuctionManager instance;
    private Auction currentAuction;
    private final AuctionJournal journal;
    private List<PastAuction> pastAuctions;
    List<ItemStack> possibleItems;
    private long lastAuction;
    private final List<String> unreadableHistory = new ArrayList<>();
    private final List<String> unreadableLoot = new ArrayList<>();

    public AuctionManager() {
        AuctionManager.instance = this;
        this.pastAuctions = new ArrayList<>();
        List<String> savedHistory = BlackMarketAuction.get().getHistory().getConfig().getStringList("history");
        if (savedHistory.isEmpty()) savedHistory = BlackMarketAuction.get().getConfig().getStringList("past-auctions");
        for (String encoded : savedHistory) {
            PastAuction past = PastAuction.fromString(encoded);
            if (past != null) this.pastAuctions.add(past);
            else {
                unreadableHistory.add(encoded);
                BlackMarketAuction.get().getLogger().warning("Preserving unreadable historical auction record");
            }
        }
        this.possibleItems = new ArrayList<>();
        List<String> configuredItems = BlackMarketAuction.get().getConfig().getStringList("items");
        if (configuredItems.isEmpty()) configuredItems = BlackMarketAuction.get().getConfig().getStringList("possible-items");
        for (String encoded : configuredItems) {
            try {
                ItemStack item = ConfigUtils.getItem(encoded);
                if (item != null && item.getType() != Material.AIR) this.possibleItems.add(item.clone());
                else unreadableLoot.add(encoded);
            } catch (RuntimeException exception) {
                unreadableLoot.add(encoded);
                BlackMarketAuction.get().getLogger().warning("Preserving unreadable auction loot entry");
            }
        }
        this.lastAuction = BlackMarketAuction.get().getConfig().getLong("last-auction");
        this.journal = new AuctionJournal(BlackMarketAuction.get().getDataFolder());
        Auction restored = journal.restore();
        if (restored != null) {
            this.currentAuction = restored;
            schedule(restored);
            BlackMarketAuction.get().getLogger().info("Resumed crash-interrupted auction " + restored.auctionUUID + " with escrow " + restored.currentBid);
        }
    }

    public void startRandomAuction() {
        if (journal.isPending()) throw new IllegalStateException("Escrow reconciliation required; preserve auction journal");
        if (currentAuction != null) throw new IllegalStateException("An auction is already active");
        if (!hasAuctionItem()) throw new IllegalStateException("No auction loot configured; use /bah additem or setnextauction");
        final int increment = BlackMarketAuction.get().getConfig().getInt("minimum-bid-increment", 100000);
        if (increment <= 0) throw new IllegalStateException("minimum-bid-increment must be positive");
        ItemStack rewardItem = BlackMarketAuction.get().getNextAuctionItem();
        if (rewardItem != null) {
            rewardItem = rewardItem.clone();
            BlackMarketAuction.get().setNextAuctionItem(null);
        }
        else if (externalLootSelected()) {
            rewardItem = BlackMarketAuction.get().getLootTableBridge().randomReward();
        }
        else {
            rewardItem = this.possibleItems.get(new Random().nextInt(this.possibleItems.size())).clone();
        }
        final Auction auction = new Auction(rewardItem, increment);
        this.lastAuction = System.currentTimeMillis();
        BlackMarketAuction.get().getConfig().set("last-auction", this.lastAuction);
        BlackMarketAuction.get().saveConfig();
        journal.save(auction, "active", null, 0);
        this.setCurrentAuction(auction);
        final ItemStack item = rewardItem;
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName == null) {
            displayName = WordUtils.capitalizeFully(item.getType().name().replace("_", " "));
        }
        Utils.sendCenteredMessage("");
        Utils.sendCenteredMessage(CC.translate("&f&l* &b&lBlack Market Auction &f&l*"));
        Utils.sendCenteredMessage(CC.translate("&b&lItem: &f&n" + displayName));
        Utils.sendCenteredMessage(CC.translate("&b&lTime: &74m 59s"));
        Utils.sendCenteredMessage(CC.translate("&7Use &b/bah &7to view and place bids."));
        Utils.sendCenteredMessage("");
        schedule(auction);
    }

    public AuctionJournal getJournal() { return journal; }

    private void schedule(final Auction auction) {
        new BukkitRunnable() {
            public void run() {
                if (currentAuction != auction || journal.isPending()) { this.cancel(); return; }
                if (auction.endsAt <= System.currentTimeMillis()) {
                    auction.onEnd();
                    this.cancel();
                }
            }
        }.runTaskTimer(BlackMarketAuction.get(), 0L, 20L);
    }

    public Auction getCurrentAuction() {
        return this.currentAuction;
    }

    public void setCurrentAuction(final Auction currentAuction) {
        this.currentAuction = currentAuction;
    }

    public static AuctionManager get() {
        return AuctionManager.instance;
    }

    public boolean addPossibleItem(ItemStack item) {
        if(item == null || item.getType() == Material.AIR)
            return false;
        if (externalLootSelected()) return BlackMarketAuction.get().getLootTableBridge().add(item);
        this.possibleItems.add(item.clone());
        saveLoot();
        return true;
    }

    public boolean removePossibleItem(ItemStack item) {
        if(item == null || item.getType() == Material.AIR)
            return false;
        if (externalLootSelected()) return BlackMarketAuction.get().getLootTableBridge().remove(item);
        if(!this.possibleItems.contains(item))
            return false;
        this.possibleItems.remove(item);
        saveLoot();
        return true;
    }

    public void addPastAuction(PastAuction auction) {
        this.pastAuctions.add(auction);
        saveHistory();
    }
    public boolean hasAuctionItem() {
        if (BlackMarketAuction.get().getNextAuctionItem() != null) return true;
        return externalLootSelected() ? BlackMarketAuction.get().getLootTableBridge().hasRewards() : !possibleItems.isEmpty();
    }

    public boolean externalLootSelected() {
        return BlackMarketAuction.get().getLootTableBridge() != null
                && BlackMarketAuction.get().getLootTableBridge().isSelected();
    }

    public List<ItemStack> getAvailableItems() {
        return externalLootSelected() ? BlackMarketAuction.get().getLootTableBridge().rewards() : getPossibleItems();
    }

    public long getLastAuction() { return lastAuction; }

    public List<PastAuction> getPastAuctions() { return Collections.unmodifiableList(pastAuctions); }

    public List<ItemStack> getPossibleItems() {
        List<ItemStack> copy = new ArrayList<>();
        possibleItems.forEach(item -> copy.add(item.clone()));
        return copy;
    }

    private void saveLoot() {
        List<String> encoded = new ArrayList<>(unreadableLoot);
        possibleItems.forEach(item -> encoded.add(ConfigUtils.toBase64(new ItemStack[]{item}, 9)));
        BlackMarketAuction.get().getConfig().set("items", encoded);
        // Keep the historical key synchronized for migrated configurations.
        BlackMarketAuction.get().getConfig().set("possible-items", encoded);
        BlackMarketAuction.get().saveConfig();
    }

    private void saveHistory() {
        List<String> encoded = new ArrayList<>(unreadableHistory);
        pastAuctions.forEach(past -> {
            String data = past.toString();
            if (data != null) encoded.add(data);
        });
        BlackMarketAuction.get().getHistory().getConfig().set("history", encoded);
        BlackMarketAuction.get().getHistory().save();
    }

    public void shutdown() {
        if (journal.isPending()) {
            BlackMarketAuction.get().getLogger().severe("Preserving interrupted auction journal; manual economy reconciliation required, no automatic refund replay");
            saveHistory();instance = null;return;
        }
        if (currentAuction != null && currentAuction.currentTopBidder != null) {
            journal.save(currentAuction, "shutdown-refund-pending", null, 0);
            EconomyResponse refund = BlackMarketAuction.get().getEconomy().depositPlayer(
                    Bukkit.getOfflinePlayer(currentAuction.currentTopBidder), currentAuction.currentBid);
            if (refund.transactionSuccess()) journal.flushEconomy(currentAuction.currentTopBidder);
            if (!refund.transactionSuccess()) {
                BlackMarketAuction.get().getLogger().severe("Unable to refund interrupted auction "
                        + currentAuction.auctionUUID + " to " + currentAuction.currentTopBidder
                        + " for " + currentAuction.currentBid + ": " + refund.errorMessage);
                return;
            }
        }
        journal.clear();
        currentAuction = null;
        saveHistory();
        instance = null;
    }

}
