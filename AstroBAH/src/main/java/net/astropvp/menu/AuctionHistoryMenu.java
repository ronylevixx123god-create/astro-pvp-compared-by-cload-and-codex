package net.astropvp.menu;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.astropvp.auction.PastAuction;
import net.astropvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** Adapts the earlier 54-slot history GUI, repairing its null access and pagination. */
public class AuctionHistoryMenu implements Menu {
    private static final int PAGE_SIZE = 45;
    private final Inventory inventory;
    private final int page;
    private final int pages;

    public AuctionHistoryMenu(int requestedPage) {
        List<PastAuction> history = BlackMarketAuction.get().getAuctionManager().getPastAuctions();
        pages = Math.max(1, (history.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        page = Math.max(1, Math.min(requestedPage, pages));
        inventory = Bukkit.createInventory(this, 54, "BAH History - Page " + page);
        for (int slot = 0; slot < PAGE_SIZE; slot++) {
            int index = history.size() - 1 - ((page - 1) * PAGE_SIZE + slot);
            if (index < 0) break;
            inventory.setItem(slot, historyItem(history.get(index)));
        }
        if (page > 1) inventory.setItem(45, button("Previous Page"));
        inventory.setItem(49, button("Back to Auction"));
        if (page < pages) inventory.setItem(53, button("Next Page"));
    }

    private static ItemStack button(String name) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack historyItem(PastAuction past) {
        ItemStack item;
        try {
            item = Utils.getItem(past.getItem()).clone();
            if (item.getType() == Material.AIR) throw new IllegalArgumentException("Empty reward");
        } catch (RuntimeException invalidRecord) {
            item = new ItemStack(Material.PAPER);
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.addAll(Arrays.asList("", "Winning Bidder: " + (past.didPlayersBid() ? past.getWinningBidder() : "No bids"),
                "Winning Bid: $" + Utils.formatMoney(past.getWinningBid()),
                "Sold: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'").withZone(ZoneOffset.UTC)
                        .format(Instant.ofEpochMilli(past.getDateSold()))));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getRawSlot() == 45 && page > 1) player.openInventory(new AuctionHistoryMenu(page - 1).getInventory());
        if (event.getRawSlot() == 53 && page < pages) player.openInventory(new AuctionHistoryMenu(page + 1).getInventory());
        if (event.getRawSlot() == 49) player.openInventory(BlackMarketAuction.get().getAuctionMenu().getInventory());
    }

    public int getPage() { return page; }
    public int getPages() { return pages; }
    @Override
    public Inventory getInventory() { return inventory; }
}
