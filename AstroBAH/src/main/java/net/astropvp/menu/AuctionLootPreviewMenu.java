package net.astropvp.menu;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

/** Public read-only preview; editing remains an explicit administrator operation. */
public class AuctionLootPreviewMenu implements Menu {
    private final Inventory inventory;
    private final int page;
    private final int pages;

    public AuctionLootPreviewMenu(int requestedPage) {
        List<ItemStack> items = BlackMarketAuction.get().getAuctionManager().getAvailableItems();
        pages = Math.max(1, (items.size() + 44) / 45);
        page = Math.max(1, Math.min(requestedPage, pages));
        inventory = Bukkit.createInventory(this, 54, "BAH Loot - Page " + page);
        for (int i = 0; i < 45 && (page - 1) * 45 + i < items.size(); i++) {
            inventory.setItem(i, items.get((page - 1) * 45 + i).clone());
        }
        if (page > 1) inventory.setItem(45, arrow("Previous Page"));
        if (page < pages) inventory.setItem(53, arrow("Next Page"));
    }

    private ItemStack arrow(String name) {
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta meta = arrow.getItemMeta();
        meta.setDisplayName(name);
        arrow.setItemMeta(meta);
        return arrow;
    }

    public void onClick(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getRawSlot() == 45 && page > 1) player.openInventory(new AuctionLootPreviewMenu(page - 1).getInventory());
        if (event.getRawSlot() == 53 && page < pages) player.openInventory(new AuctionLootPreviewMenu(page + 1).getInventory());
    }

    public Inventory getInventory() { return inventory; }
}
